CREATE UNIQUE INDEX IF NOT EXISTS uq_donor_status_name_active
    ON donor_mgmt.donor_status (lower(dst_name))
    WHERE dst_deleted_at IS NULL;