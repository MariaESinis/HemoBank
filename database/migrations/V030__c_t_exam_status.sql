CREATE TABLE IF NOT EXISTS donation.exam_status (
    exs_id          INTEGER GENERATED ALWAYS AS IDENTITY (
                        SEQUENCE NAME donation.seq_exam_status_exs_id
                        START WITH 0
                        MINVALUE 0
                        INCREMENT BY 1
                        NO CYCLE
                    ),
    exs_name        VARCHAR(50)  NOT NULL,
    exs_description VARCHAR(255),

    exs_created_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exs_updated_at  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exs_deleted_at  TIMESTAMPTZ,

    CONSTRAINT pk_exam_status
        PRIMARY KEY (exs_id),

    CONSTRAINT ck_exam_status_name_not_blank
        CHECK (btrim(exs_name) <> ''),

    CONSTRAINT ck_exam_status_updated_at
        CHECK (exs_updated_at >= exs_created_at),
        
    CONSTRAINT ck_exam_status_deleted_at
        CHECK (exs_deleted_at IS NULL OR exs_deleted_at >= exs_created_at)
);
