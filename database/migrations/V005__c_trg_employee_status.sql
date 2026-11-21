DROP TRIGGER IF EXISTS trg_employee_status_upd ON hr.employee_status;
CREATE TRIGGER trg_employee_status_upd
    BEFORE UPDATE ON hr.employee_status
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('ems_updated_at');
