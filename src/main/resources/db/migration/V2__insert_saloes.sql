-- =============================================================
-- V2__add_superadmin_to_users_plan_check.sql
-- O enum Plan no código Java contém SUPERADMIN mas o constraint
-- de produção não. Corrige o check constraint para incluir
-- SUPERADMIN, necessário para criação via script SQL.
-- =============================================================

ALTER TABLE public.users
DROP CONSTRAINT users_plan_check;

ALTER TABLE public.users
    ADD CONSTRAINT users_plan_check CHECK (((plan)::text = ANY ((ARRAY[
    'GOLD'::character varying,
    'BLACK'::character varying,
    'INFINITE'::character varying,
    'ADMIN'::character varying,
    'BARMAN'::character varying,
    'CONCIERGE'::character varying,
    'SUPERADMIN'::character varying
    ])::text[])));
