CREATE TABLE IF NOT EXISTS stock.inventory_status (
    ivs_id          INTEGER GENERATED ALWAYS AS IDENTITY (
                        SEQUENCE NAME stock.seq_inventory_status_ivs_id
                        START WITH 0
                        MINVALUE 0
                        INCREMENT BY 1
                        NO CYCLE
                    ),
    ivs_name        VARCHAR(50)  NOT NULL,
    ivs_description VARCHAR(255),

    ivs_created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ivs_updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ivs_deleted_at  TIMESTAMPTZ,

    CONSTRAINT pk_inventory_status
        PRIMARY KEY (ivs_id),

    CONSTRAINT ck_inventory_status_name_not_blank
        CHECK (btrim(ivs_name) <> ''),

    CONSTRAINT ck_inventory_status_updated_at
        CHECK (ivs_updated_at >= ivs_created_at),

    CONSTRAINT ck_inventory_status_deleted_at
        CHECK (ivs_deleted_at IS NULL OR ivs_deleted_at >= ivs_created_at)
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_inventory_status_name_active
    ON stock.inventory_status (lower(ivs_name))
    WHERE ivs_deleted_at IS NULL;

DROP TRIGGER IF EXISTS trg_inventory_status_upd ON stock.inventory_status;
CREATE TRIGGER trg_inventory_status_upd
    BEFORE UPDATE ON stock.inventory_status
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('ivs_updated_at');

CREATE TABLE IF NOT EXISTS stock.inventory (
    inv_id         INTEGER GENERATED ALWAYS AS IDENTITY (
                       SEQUENCE NAME stock.seq_inventory_inv_id
                       START WITH 0
                       MINVALUE 0
                       INCREMENT BY 1
                       NO CYCLE
                   ),
    inv_labeling   VARCHAR(10),
    inv_type       VARCHAR(20),
    inv_quantity   INTEGER      NOT NULL DEFAULT 0,

    inv_created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    inv_updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    inv_deleted_at TIMESTAMPTZ,

    inv_ivs_id     INTEGER      NOT NULL,

    CONSTRAINT pk_inventory
        PRIMARY KEY (inv_id),

    CONSTRAINT fk_inventory_inventory_status
        FOREIGN KEY (inv_ivs_id) REFERENCES stock.inventory_status (ivs_id),
    
    CONSTRAINT ck_inventory_labeling_not_blank
        CHECK (inv_labeling IS NULL OR btrim(inv_labeling) <> ''),

    CONSTRAINT ck_inventory_type_not_blank
        CHECK (inv_type IS NULL OR btrim(inv_type) <> ''),

    CONSTRAINT ck_inventory_quantity_non_negative
        CHECK (inv_quantity >= 0),

    CONSTRAINT ck_inventory_updated_at
        CHECK (inv_updated_at >= inv_created_at),

    CONSTRAINT ck_inventory_deleted_at
        CHECK (inv_deleted_at IS NULL OR inv_deleted_at >= inv_created_at)
);

CREATE INDEX IF NOT EXISTS ix_inventory_ivs_id
    ON stock.inventory (inv_ivs_id);

CREATE INDEX IF NOT EXISTS ix_inventory_labeling_type
    ON stock.inventory (inv_labeling, inv_type);

DROP TRIGGER IF EXISTS trg_inventory_upd ON stock.inventory;
CREATE TRIGGER trg_inventory_upd
    BEFORE UPDATE ON stock.inventory
    FOR EACH ROW EXECUTE FUNCTION core.set_updated_at('inv_updated_at');