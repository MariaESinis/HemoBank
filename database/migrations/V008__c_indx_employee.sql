CREATE UNIQUE INDEX IF NOT EXISTS uq_employee_email_active
    ON hr.employee (lower(emp_email))
    WHERE emp_deleted_at IS NULL AND emp_email IS NOT NULL;