CREATE TABLE IF NOT EXISTS hr.employee_status (
    ems_id          INTEGER GENERATED ALWAYS AS IDENTITY (
                        SEQUENCE NAME hr.seq_employee_status_ems_id
                        START WITH 0
                        MINVALUE 0
                        INCREMENT BY 1
                        NO CYCLE
                    ),

    ems_name        VARCHAR(50)     NOT NULL,
    ems_description VARCHAR(255)        NULL,

    ems_created_at  TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ems_updated_at  TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ems_deleted_at  TIMESTAMPTZ         NULL,

    CONSTRAINT pk_employee_status
        PRIMARY KEY (ems_id),

    CONSTRAINT ck_employee_status_id_non_negative
        CHECK (ems_id >= 0),

    CONSTRAINT ck_employee_status_name_not_blank
        CHECK (btrim(ems_name) <> ''),

    CONSTRAINT ck_employee_status_updated_at
        CHECK (ems_updated_at >= ems_created_at),
        
    CONSTRAINT ck_employee_status_deleted_at
        CHECK (ems_deleted_at IS NULL OR ems_deleted_at >= ems_created_at)
);