DROP TRIGGER IF EXISTS trg_appointment_upd ON donor_mgmt.appointment;
CREATE TRIGGER trg_appointment_upd
    BEFORE UPDATE ON donor_mgmt.appointment
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('app_updated_at');