CREATE UNIQUE INDEX IF NOT EXISTS uq_donor_cpf_active
    ON donor_mgmt.donor (don_cpf)
    WHERE don_deleted_at IS NULL;