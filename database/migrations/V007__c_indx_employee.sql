CREATE UNIQUE INDEX IF NOT EXISTS uq_employee_cpf_active
    ON hr.employee (emp_cpf)
    WHERE emp_deleted_at IS NULL;