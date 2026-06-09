-- =============================================================
-- V3__add_version_to_events.sql
-- Adiciona coluna de controle de versão para Optimistic Locking
-- JPA (@Version). Valor inicial 0 para todos os registros
-- existentes é o comportamento correto do Hibernate.
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

ALTER TABLE public.events
    ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;
