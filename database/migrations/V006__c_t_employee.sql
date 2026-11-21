CREATE TABLE IF NOT EXISTS hr.employee (
    emp_id          INTEGER GENERATED ALWAYS AS IDENTITY (
                        SEQUENCE NAME hr.seq_employee_emp_id
                        START WITH 0
                        MINVALUE 0
                        INCREMENT BY 1
                        NO CYCLE
                    ),
    emp_name        VARCHAR(150) NOT NULL,
    emp_cpf         CHAR(11)     NOT NULL,
    emp_position    VARCHAR(100),
    emp_phone       VARCHAR(20),
    emp_email       VARCHAR(150),
    emp_hire_date   TIMESTAMPTZ,
    emp_salary      DECIMAL(10,2),

    emp_created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    emp_updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    emp_deleted_at  TIMESTAMPTZ,

    emp_ems_id      INTEGER      NOT NULL,

    CONSTRAINT pk_employee
        PRIMARY KEY (emp_id),

    CONSTRAINT fk_employee_employee_status
        FOREIGN KEY (emp_ems_id) REFERENCES hr.employee_status (ems_id),

    CONSTRAINT ck_employee_name_not_blank
        CHECK (btrim(emp_name) <> ''),

    CONSTRAINT ck_employee_cpf_format
        CHECK (emp_cpf ~ '^[0-9]{11}$'),

    CONSTRAINT ck_employee_position_not_blank
        CHECK (emp_position IS NULL OR btrim(emp_position) <> ''),

    CONSTRAINT ck_employee_phone_format
        CHECK (emp_phone IS NULL OR emp_phone ~ '^[0-9+()\- ]{8,20}$'),

    CONSTRAINT ck_employee_email_format
        CHECK (emp_email IS NULL OR emp_email ~* '^[^@[:space:]]+@[^@[:space:]]+\.[^@[:space:]]+$'),
    
    CONSTRAINT ck_employee_salary_non_negative
        CHECK (emp_salary IS NULL OR emp_salary >= 0),

    CONSTRAINT ck_employee_updated_at
        CHECK (emp_updated_at >= emp_created_at),
        
    CONSTRAINT ck_employee_deleted_at
        CHECK (emp_deleted_at IS NULL OR emp_deleted_at >= emp_created_at)
);