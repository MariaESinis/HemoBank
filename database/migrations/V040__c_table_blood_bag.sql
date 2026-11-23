CREATE TABLE IF NOT EXISTS stock.blood_bag (
    bbg_id              INTEGER GENERATED ALWAYS AS IDENTITY (
                            SEQUENCE NAME stock.seq_blood_bag_bbg_id
                            START WITH 0
                            MINVALUE 0
                            INCREMENT BY 1
                            NO CYCLE
                        ),
    bbg_generated_date  TIMESTAMPTZ  NOT NULL,
    bbg_quantity_ml     INTEGER,
    bbg_type            VARCHAR(20),
    bbg_observations    VARCHAR(500),
    bbg_labeling        VARCHAR(10),
    bbg_expiration_date TIMESTAMPTZ,

    bbg_created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bbg_updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bbg_deleted_at      TIMESTAMPTZ,

    bbg_dnt_id          INTEGER      NOT NULL,
    bbg_inv_id          INTEGER      NOT NULL,

    CONSTRAINT pk_blood_bag
        PRIMARY KEY (bbg_id),

    CONSTRAINT fk_blood_bag_donation
        FOREIGN KEY (bbg_dnt_id) REFERENCES donation.donation (dnt_id),
    
    CONSTRAINT fk_blood_bag_inventory
        FOREIGN KEY (bbg_inv_id) REFERENCES stock.inventory (inv_id),
    
    CONSTRAINT ck_blood_bag_quantity_ml_positive
        CHECK (bbg_quantity_ml IS NULL OR bbg_quantity_ml > 0),
    
    CONSTRAINT ck_blood_bag_type_not_blank
        CHECK (bbg_type IS NULL OR btrim(bbg_type) <> ''),
    
    CONSTRAINT ck_blood_bag_observations_not_blank
        CHECK (bbg_observations IS NULL OR btrim(bbg_observations) <> ''),
    
    CONSTRAINT ck_blood_bag_labeling_not_blank
        CHECK (bbg_labeling IS NULL OR btrim(bbg_labeling) <> ''),
    
    CONSTRAINT ck_blood_bag_expiration_after_generated
        CHECK (bbg_expiration_date IS NULL OR bbg_expiration_date >= bbg_generated_date),
    
    CONSTRAINT ck_blood_bag_updated_at
        CHECK (bbg_updated_at >= bbg_created_at),
    
    CONSTRAINT ck_blood_bag_deleted_at
        CHECK (bbg_deleted_at IS NULL OR bbg_deleted_at >= bbg_created_at)
);

CREATE INDEX IF NOT EXISTS ix_blood_bag_dnt_id
    ON stock.blood_bag (bbg_dnt_id);

CREATE INDEX IF NOT EXISTS ix_blood_bag_inv_id
    ON stock.blood_bag (bbg_inv_id);

CREATE INDEX IF NOT EXISTS ix_blood_bag_expiration_date
    ON stock.blood_bag (bbg_expiration_date);

DROP TRIGGER IF EXISTS trg_blood_bag_upd ON stock.blood_bag;
CREATE TRIGGER trg_blood_bag_upd
    BEFORE UPDATE ON stock.blood_bag
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('bbg_updated_at');

CREATE TABLE IF NOT EXISTS stock.bag_history (
    bgh_id         INTEGER GENERATED ALWAYS AS IDENTITY (
                       SEQUENCE NAME stock.seq_bag_history_bgh_id
                       START WITH 0
                       MINVALUE 0
                       INCREMENT BY 1
                       NO CYCLE
                   ),
    bgh_entry_date TIMESTAMPTZ  NOT NULL,
    bgh_exit_date  TIMESTAMPTZ,
    bgh_reason     VARCHAR(255),

    bgh_created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bgh_updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bgh_deleted_at TIMESTAMPTZ,

    bgh_bbg_id     INTEGER      NOT NULL,

    CONSTRAINT pk_bag_history
        PRIMARY KEY (bgh_id),
    
    CONSTRAINT fk_bag_history_blood_bag
        FOREIGN KEY (bgh_bbg_id) REFERENCES stock.blood_bag (bbg_id),
   
    CONSTRAINT ck_bag_history_exit_after_entry
        CHECK (bgh_exit_date IS NULL OR bgh_exit_date >= bgh_entry_date),
    
    CONSTRAINT ck_bag_history_reason_not_blank
        CHECK (bgh_reason IS NULL OR btrim(bgh_reason) <> ''),
    
    CONSTRAINT ck_bag_history_updated_at
        CHECK (bgh_updated_at >= bgh_created_at),
    
    CONSTRAINT ck_bag_history_deleted_at
        CHECK (bgh_deleted_at IS NULL OR bgh_deleted_at >= bgh_created_at)
);

CREATE INDEX IF NOT EXISTS ix_bag_history_bbg_id
    ON stock.bag_history (bgh_bbg_id);

