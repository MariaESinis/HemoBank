CREATE TABLE IF NOT EXISTS donation.exam (
    exm_id           INTEGER GENERATED ALWAYS AS IDENTITY (
                         SEQUENCE NAME donation.seq_exam_exm_id
                         START WITH 0
                         MINVALUE 0
                         INCREMENT BY 1
                         NO CYCLE
                     ),
    exm_type         VARCHAR(200)  NOT NULL,
    exm_result       VARCHAR(1000),
    exm_date         TIMESTAMPTZ,
    exm_observations VARCHAR(1000),

    exm_created_at   TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exm_updated_at   TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exm_deleted_at   TIMESTAMPTZ,

    exm_exs_id       INTEGER       NOT NULL,
    exm_dnt_id       INTEGER       NOT NULL,

    CONSTRAINT pk_exam
        PRIMARY KEY (exm_id),
    
    CONSTRAINT fk_exam_exam_status
        FOREIGN KEY (exm_exs_id) REFERENCES donation.exam_status (exs_id),
    
    CONSTRAINT fk_exam_donation
        FOREIGN KEY (exm_dnt_id) REFERENCES donation.donation (dnt_id),

    CONSTRAINT ck_exam_type_not_blank
        CHECK (btrim(exm_type) <> ''),
    
    CONSTRAINT ck_exam_result_not_blank
        CHECK (exm_result IS NULL OR btrim(exm_result) <> ''),
    
    CONSTRAINT ck_exam_observations_not_blank
        CHECK (exm_observations IS NULL OR btrim(exm_observations) <> ''),
    
    CONSTRAINT ck_exam_updated_at
        CHECK (exm_updated_at >= exm_created_at),
    
    CONSTRAINT ck_exam_deleted_at
        CHECK (exm_deleted_at IS NULL OR exm_deleted_at >= exm_created_at)
);