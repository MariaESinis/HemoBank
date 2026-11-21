DROP TRIGGER IF EXISTS trg_donation_upd ON donation.donation;
CREATE TRIGGER trg_donation_upd
    BEFORE UPDATE ON donation.donation
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('dnt_updated_at');