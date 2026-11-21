DROP TRIGGER IF EXISTS trg_donor_upd ON donor_mgmt.donor;
CREATE TRIGGER trg_donor_upd
    BEFORE UPDATE ON donor_mgmt.donor
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('don_updated_at');