CREATE TABLE IF NOT EXISTS donation.donation_status (
    dts_id          INTEGER GENERATED ALWAYS AS IDENTITY (
                        SEQUENCE NAME donation.seq_donation_status_dts_id
                        START WITH 0
                        MINVALUE 0
                        INCREMENT BY 1
                        NO CYCLE
                    ),
    dts_name        VARCHAR(50)  NOT NULL,
    dts_description VARCHAR(255),

    dts_created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dts_updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dts_deleted_at  TIMESTAMPTZ,

    CONSTRAINT pk_donation_status
        PRIMARY KEY (dts_id),

    CONSTRAINT ck_donation_status_name_not_blank
        CHECK (btrim(dts_name) <> ''),

    CONSTRAINT ck_donation_status_updated_at
        CHECK (dts_updated_at >= dts_created_at),
        
    CONSTRAINT ck_donation_status_deleted_at
        CHECK (dts_deleted_at IS NULL OR dts_deleted_at >= dts_created_at)
);