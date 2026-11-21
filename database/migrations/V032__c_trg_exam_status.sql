DROP TRIGGER IF EXISTS trg_exam_status_upd ON donation.exam_status;
CREATE TRIGGER trg_exam_status_upd
    BEFORE UPDATE ON donation.exam_status
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('exs_updated_at');