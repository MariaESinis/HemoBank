ALTER TABLE hr.employee
    DROP CONSTRAINT IF EXISTS ck_employee_login_requires_access_level,
    DROP CONSTRAINT IF EXISTS fk_employee_access_level;

DROP INDEX IF EXISTS hr.ix_employee_acl_id;

ALTER TABLE hr.employee
    DROP COLUMN IF EXISTS emp_acl_id;

DROP TABLE IF EXISTS hr.access_level;

ALTER TABLE hr.employee
    ADD COLUMN IF NOT EXISTS emp_auth_id      UUID,
    ADD COLUMN IF NOT EXISTS emp_access_level VARCHAR(20);

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_employee_auth_users') THEN
        ALTER TABLE hr.employee
            ADD CONSTRAINT fk_employee_auth_users
            FOREIGN KEY (emp_auth_id) REFERENCES auth.users (id) ON DELETE SET NULL;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'ck_employee_access_level_valid') THEN
        ALTER TABLE hr.employee
            ADD CONSTRAINT ck_employee_access_level_valid
            CHECK (emp_access_level IS NULL
                   OR emp_access_level IN ('admin', 'manager', 'operator'));
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'ck_employee_login_requires_access_level') THEN
        ALTER TABLE hr.employee
            ADD CONSTRAINT ck_employee_login_requires_access_level
            CHECK (emp_auth_id IS NULL OR emp_access_level IS NOT NULL);
    END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS uq_employee_auth_id
    ON hr.employee (emp_auth_id)
    WHERE emp_auth_id IS NOT NULL;

ALTER TABLE hr.employee ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS employee_select_own ON hr.employee;
CREATE POLICY employee_select_own
    ON hr.employee
    FOR SELECT
    TO authenticated
    USING (emp_auth_id = (SELECT auth.uid()));

GRANT USAGE ON SCHEMA hr TO authenticated;
GRANT SELECT (emp_id, emp_name, emp_position, emp_phone, emp_email,
              emp_ems_id, emp_access_level)
    ON hr.employee TO authenticated;