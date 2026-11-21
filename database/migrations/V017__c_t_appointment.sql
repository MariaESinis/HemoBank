CREATE TABLE IF NOT EXISTS donor_mgmt.appointment (
    app_id           INTEGER GENERATED ALWAYS AS IDENTITY (
                         SEQUENCE NAME donor_mgmt.seq_appointment_app_id
                         START WITH 0
                         MINVALUE 0
                         INCREMENT BY 1
                         NO CYCLE
                     ),
    app_date         TIMESTAMPTZ  NOT NULL,
    app_time         TIMESTAMPTZ,
    app_observations VARCHAR(500),

    app_created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    app_updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    app_deleted_at   TIMESTAMPTZ,

    app_don_id       INTEGER      NOT NULL,

    CONSTRAINT pk_appointment
        PRIMARY KEY (app_id),
    
    CONSTRAINT fk_appointment_donor
        FOREIGN KEY (app_don_id) REFERENCES donor_mgmt.donor (don_id),
 
    CONSTRAINT ck_appointment_observations_not_blank
        CHECK (app_observations IS NULL OR btrim(app_observations) <> ''),
    
    CONSTRAINT ck_appointment_updated_at
        CHECK (app_updated_at >= app_created_at),
    
    CONSTRAINT ck_appointment_deleted_at
        CHECK (app_deleted_at IS NULL OR app_deleted_at >= app_created_at)
);
