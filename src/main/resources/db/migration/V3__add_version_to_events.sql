-- =============================================================
-- V3__add_version_to_events.sql
-- Adiciona coluna de controle de versão para Optimistic Locking
-- JPA (@Version). Valor inicial 0 para todos os registros
-- existentes é o comportamento correto do Hibernate.
-- =============================================================

ALTER TABLE public.events
    ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;
