CREATE UNIQUE INDEX IF NOT EXISTS uq_employee_status_name_active
    ON hr.employee_status (lower(ems_name))
    WHERE ems_deleted_at IS NULL;