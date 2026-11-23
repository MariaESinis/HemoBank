-- Procedure registrar funcionario
CREATE OR REPLACE PROCEDURE hr.sp_register_employee(
    p_name          VARCHAR,
    p_cpf           VARCHAR,
    p_position      VARCHAR,
    p_phone         VARCHAR,
    p_email         VARCHAR,
    p_hire_date     DATE,
    p_salary        NUMERIC,
    p_ems_id        INTEGER,
    p_auth_id       UUID    DEFAULT NULL,
    p_access_level  VARCHAR DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_constraint TEXT;
BEGIN
    INSERT INTO hr.employee (
        emp_name, emp_cpf, emp_position, emp_phone, emp_email,
        emp_hire_date, emp_salary, emp_ems_id, emp_auth_id, emp_access_level
    )
    VALUES (
        btrim(p_name),
        regexp_replace(COALESCE(p_cpf, ''), '\D', '', 'g'),  
        NULLIF(btrim(p_position), ''),
        NULLIF(btrim(p_phone), ''),
        lower(NULLIF(btrim(p_email), '')),
        p_hire_date,
        p_salary,
        p_ems_id,
        p_auth_id,
        NULLIF(btrim(p_access_level), '')
    );
EXCEPTION
    WHEN unique_violation THEN
        GET STACKED DIAGNOSTICS v_constraint = CONSTRAINT_NAME;
        IF v_constraint = 'uq_employee_cpf_active' THEN
            RAISE EXCEPTION 'CPF already registered.' USING ERRCODE = '23505';
        ELSIF v_constraint = 'uq_employee_email_active' THEN
            RAISE EXCEPTION 'E-mail already registered.' USING ERRCODE = '23505';
        ELSIF v_constraint = 'uq_employee_auth_id' THEN
            RAISE EXCEPTION 'This login is already linked to another employee.'
                USING ERRCODE = '23505';
        ELSE
            RAISE;
        END IF;

    WHEN foreign_key_violation THEN
        GET STACKED DIAGNOSTICS v_constraint = CONSTRAINT_NAME;
        IF v_constraint = 'fk_employee_employee_status' THEN
            RAISE EXCEPTION 'Employee status does not exist.' USING ERRCODE = '23503';
        ELSIF v_constraint = 'fk_employee_auth_users' THEN
            RAISE EXCEPTION 'Authentication user does not exist.' USING ERRCODE = '23503';
        ELSE
            RAISE;
        END IF;

    WHEN check_violation THEN
        GET STACKED DIAGNOSTICS v_constraint = CONSTRAINT_NAME;
        IF v_constraint = 'ck_employee_login_requires_access_level' THEN
            RAISE EXCEPTION 'An employee with login requires an access level.'
                USING ERRCODE = '23514';
        ELSIF v_constraint = 'ck_employee_access_level_valid' THEN
            RAISE EXCEPTION 'Invalid access level.' USING ERRCODE = '23514';
        ELSE
            RAISE;
        END IF;
END;
$$;

-- Procedure achar o funcionario por email
CREATE OR REPLACE FUNCTION hr.fn_get_employee_by_email(p_email VARCHAR)
RETURNS TABLE (
    emp_id INTEGER, emp_name VARCHAR, emp_cpf CHAR, emp_position VARCHAR,
    emp_phone VARCHAR, emp_email VARCHAR, emp_hire_date TIMESTAMPTZ,
    emp_ems_id INTEGER, ems_name VARCHAR,
    emp_auth_id UUID, emp_access_level VARCHAR
)
LANGUAGE sql
STABLE
AS $$
    SELECT e.emp_id, e.emp_name, e.emp_cpf, e.emp_position,
           e.emp_phone, e.emp_email, e.emp_hire_date,
           e.emp_ems_id, s.ems_name,
           e.emp_auth_id, e.emp_access_level
    FROM hr.employee e
    JOIN hr.employee_status s ON s.ems_id = e.emp_ems_id
    WHERE lower(e.emp_email) = lower(btrim(p_email))
      AND e.emp_deleted_at IS NULL;
$$;

-- Função funcionario por ID
CREATE OR REPLACE FUNCTION hr.fn_get_employee_by_auth_id(p_auth_id UUID)
RETURNS TABLE (
    emp_id INTEGER, emp_name VARCHAR, emp_cpf CHAR, emp_position VARCHAR,
    emp_phone VARCHAR, emp_email VARCHAR, emp_hire_date TIMESTAMPTZ,
    emp_ems_id INTEGER, ems_name VARCHAR,
    emp_auth_id UUID, emp_access_level VARCHAR
)
LANGUAGE sql
STABLE
AS $$
    SELECT e.emp_id, e.emp_name, e.emp_cpf, e.emp_position,
           e.emp_phone, e.emp_email, e.emp_hire_date,
           e.emp_ems_id, s.ems_name,
           e.emp_auth_id, e.emp_access_level
    FROM hr.employee e
    JOIN hr.employee_status s ON s.ems_id = e.emp_ems_id
    WHERE e.emp_auth_id = p_auth_id
      AND e.emp_deleted_at IS NULL;
$$;

-- Função lista funcionario
CREATE OR REPLACE FUNCTION hr.fn_list_employees()
RETURNS TABLE (
    emp_id INTEGER, emp_name VARCHAR, emp_cpf CHAR, emp_position VARCHAR,
    emp_phone VARCHAR, emp_email VARCHAR, emp_hire_date TIMESTAMPTZ,
    emp_ems_id INTEGER, ems_name VARCHAR,
    emp_auth_id UUID, emp_access_level VARCHAR
)
LANGUAGE sql
STABLE
AS $$
    SELECT e.emp_id, e.emp_name, e.emp_cpf, e.emp_position,
           e.emp_phone, e.emp_email, e.emp_hire_date,
           e.emp_ems_id, s.ems_name,
           e.emp_auth_id, e.emp_access_level
    FROM hr.employee e
    JOIN hr.employee_status s ON s.ems_id = e.emp_ems_id
    WHERE e.emp_deleted_at IS NULL
    ORDER BY e.emp_id;
$$;

-- Parte de permissoes
REVOKE EXECUTE ON PROCEDURE hr.sp_register_employee(
    VARCHAR, VARCHAR, VARCHAR, VARCHAR, VARCHAR, DATE, NUMERIC, INTEGER, UUID, VARCHAR
) FROM PUBLIC, anon, authenticated;

REVOKE EXECUTE ON FUNCTION hr.fn_get_employee_by_email(VARCHAR)
    FROM PUBLIC, anon, authenticated;
REVOKE EXECUTE ON FUNCTION hr.fn_get_employee_by_auth_id(UUID)
    FROM PUBLIC, anon, authenticated;
REVOKE EXECUTE ON FUNCTION hr.fn_list_employees()
    FROM PUBLIC, anon, authenticated;

GRANT USAGE ON SCHEMA hr TO service_role;
GRANT SELECT, INSERT ON hr.employee        TO service_role;
GRANT SELECT         ON hr.employee_status TO service_role;

GRANT EXECUTE ON PROCEDURE hr.sp_register_employee(
    VARCHAR, VARCHAR, VARCHAR, VARCHAR, VARCHAR, DATE, NUMERIC, INTEGER, UUID, VARCHAR
) TO service_role;

GRANT EXECUTE ON FUNCTION hr.fn_get_employee_by_email(VARCHAR) TO service_role;
GRANT EXECUTE ON FUNCTION hr.fn_get_employee_by_auth_id(UUID)  TO service_role;
GRANT EXECUTE ON FUNCTION hr.fn_list_employees()               TO service_role;