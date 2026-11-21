CREATE TABLE IF NOT EXISTS donation.donation (
    dnt_id              INTEGER GENERATED ALWAYS AS IDENTITY (
                            SEQUENCE NAME donation.seq_donation_dnt_id
                            START WITH 0
                            MINVALUE 0
                            INCREMENT BY 1
                            NO CYCLE
                        ),
    dnt_donation_date   TIMESTAMPTZ  NOT NULL,
    dnt_quantity_ml     INTEGER,
    dnt_donation_type   VARCHAR(50),
    dnt_observations    VARCHAR(500),
    dnt_bag_type        VARCHAR(10),
    dnt_type            VARCHAR(20),
    dnt_expiration_date TIMESTAMPTZ,

    dnt_created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dnt_updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dnt_deleted_at      TIMESTAMPTZ,

    dnt_dts_id          INTEGER      NOT NULL,
    dnt_don_id          INTEGER      NOT NULL,
    dnt_emp_id          INTEGER      NOT NULL,

    CONSTRAINT pk_donation
        PRIMARY KEY (dnt_id),

    CONSTRAINT fk_donation_donation_status
        FOREIGN KEY (dnt_dts_id) REFERENCES donation.donation_status (dts_id),
    
    CONSTRAINT fk_donation_donor
        FOREIGN KEY (dnt_don_id) REFERENCES donor_mgmt.donor (don_id),
    
    CONSTRAINT fk_donation_employee
        FOREIGN KEY (dnt_emp_id) REFERENCES hr.employee (emp_id),

    CONSTRAINT ck_donation_quantity_ml_positive
        CHECK (dnt_quantity_ml IS NULL OR dnt_quantity_ml > 0),
    
    CONSTRAINT ck_donation_donation_type_not_blank
        CHECK (dnt_donation_type IS NULL OR btrim(dnt_donation_type) <> ''),
    
    CONSTRAINT ck_donation_observations_not_blank
        CHECK (dnt_observations IS NULL OR btrim(dnt_observations) <> ''),
    
    CONSTRAINT ck_donation_bag_type_not_blank
        CHECK (dnt_bag_type IS NULL OR btrim(dnt_bag_type) <> ''),
    
    CONSTRAINT ck_donation_type_not_blank
        CHECK (dnt_type IS NULL OR btrim(dnt_type) <> ''),
    
    CONSTRAINT ck_donation_expiration_after_donation
        CHECK (dnt_expiration_date IS NULL OR dnt_expiration_date >= dnt_donation_date),
    
    CONSTRAINT ck_donation_updated_at
        CHECK (dnt_updated_at >= dnt_created_at),
    
    CONSTRAINT ck_donation_deleted_at
        CHECK (dnt_deleted_at IS NULL OR dnt_deleted_at >= dnt_created_at)
);
