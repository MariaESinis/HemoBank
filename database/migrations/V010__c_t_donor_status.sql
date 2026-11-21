CREATE TABLE IF NOT EXISTS donor_mgmt.donor_status (
    dst_id          INTEGER GENERATED ALWAYS AS IDENTITY (
                        SEQUENCE NAME donor_mgmt.seq_donor_status_dst_id
                        START WITH 0
                        MINVALUE 0
                        INCREMENT BY 1
                        NO CYCLE
                    ),
    dst_name        VARCHAR(50)  NOT NULL,
    dst_description VARCHAR(255),

    dst_created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dst_updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dst_deleted_at  TIMESTAMPTZ,

    CONSTRAINT pk_donor_status
        PRIMARY KEY (dst_id),

    CONSTRAINT ck_donor_status_name_not_blank
        CHECK (btrim(dst_name) <> ''),

    CONSTRAINT ck_donor_status_updated_at
        CHECK (dst_updated_at >= dst_created_at),

    CONSTRAINT ck_donor_status_deleted_at
        CHECK (dst_deleted_at IS NULL OR dst_deleted_at >= dst_created_at)
);
