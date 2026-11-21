CREATE UNIQUE INDEX IF NOT EXISTS uq_donation_status_name_active
    ON donation.donation_status (lower(dts_name))
    WHERE dts_deleted_at IS NULL;
