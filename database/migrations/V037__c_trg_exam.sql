DROP TRIGGER IF EXISTS trg_exam_upd ON donation.exam;
CREATE TRIGGER trg_exam_upd
    BEFORE UPDATE ON donation.exam
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('exm_updated_at');