CREATE UNIQUE INDEX IF NOT EXISTS uq_exam_status_name_active
    ON donation.exam_status (lower(exs_name))
    WHERE exs_deleted_at IS NULL;