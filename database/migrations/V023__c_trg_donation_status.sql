DROP TRIGGER IF EXISTS trg_donation_status_upd ON donation.donation_status;
CREATE TRIGGER trg_donation_status_upd
    BEFORE UPDATE ON donation.donation_status
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('dts_updated_at');