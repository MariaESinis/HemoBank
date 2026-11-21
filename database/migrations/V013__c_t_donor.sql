CREATE TABLE IF NOT EXISTS donor_mgmt.donor (
    don_id          INTEGER GENERATED ALWAYS AS IDENTITY (
                        SEQUENCE NAME donor_mgmt.seq_donor_don_id
                        START WITH 0
                        MINVALUE 0
                        INCREMENT BY 1
                        NO CYCLE
                    ),
    don_name        VARCHAR(150) NOT NULL,
    don_cpf         CHAR(11)     NOT NULL,
    don_birth_date  TIMESTAMPTZ,
    don_gender      VARCHAR(20),
    don_phone       VARCHAR(20),
    don_email       VARCHAR(150),

    don_created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    don_updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    don_deleted_at  TIMESTAMPTZ,

    don_dst_id      INTEGER      NOT NULL,

    CONSTRAINT pk_donor
        PRIMARY KEY (don_id),

    CONSTRAINT fk_donor_donor_status
        FOREIGN KEY (don_dst_id) REFERENCES donor_mgmt.donor_status (dst_id),

    CONSTRAINT ck_donor_name_not_blank
        CHECK (btrim(don_name) <> ''),

    CONSTRAINT ck_donor_cpf_format
        CHECK (don_cpf ~ '^[0-9]{11}$'),

    CONSTRAINT ck_donor_gender_not_blank
        CHECK (don_gender IS NULL OR btrim(don_gender) <> ''),

    CONSTRAINT ck_donor_phone_format
        CHECK (don_phone IS NULL OR don_phone ~ '^[0-9+()\- ]{8,20}$'),

    CONSTRAINT ck_donor_email_format
        CHECK (don_email IS NULL OR don_email ~* '^[^@[:space:]]+@[^@[:space:]]+\.[^@[:space:]]+$'),
    
    CONSTRAINT ck_donor_updated_at
        CHECK (don_updated_at >= don_created_at),
        
    CONSTRAINT ck_donor_deleted_at
        CHECK (don_deleted_at IS NULL OR don_deleted_at >= don_created_at)
);