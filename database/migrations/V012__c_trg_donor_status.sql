DROP TRIGGER IF EXISTS trg_donor_status_upd ON donor_mgmt.donor_status;
CREATE TRIGGER trg_donor_status_upd
    BEFORE UPDATE ON donor_mgmt.donor_status
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('dst_updated_at');