-- V2__insert_saloes.sql
INSERT INTO public.salon (id, totem_identifier)
VALUES
    (gen_random_uuid()::text, 'TOTEM-SALAO-A'),
    (gen_random_uuid()::text, 'TOTEM-SALAO-B')
    ON CONFLICT DO NOTHING;