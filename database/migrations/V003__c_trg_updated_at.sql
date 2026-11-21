CREATE OR REPLACE FUNCTION core.set_updated_at()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    NEW := jsonb_populate_record(
        NEW,
        jsonb_build_object(TG_ARGV[0], CURRENT_TIMESTAMP)
    );
    RETURN NEW;
END;
$$;