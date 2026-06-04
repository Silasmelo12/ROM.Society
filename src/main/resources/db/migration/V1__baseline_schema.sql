-- =============================================================
-- V1__baseline_schema.sql
-- Baseline gerado do schema REAL de produção (Cloud SQL PostgreSQL 17).
-- Este script representa o estado exato do banco em produção.
-- NÃO executar manualmente — gerenciado pelo Flyway.
-- =============================================================

CREATE TABLE IF NOT EXISTS public.addresses (
    id character varying(255) NOT NULL,
    bairro character varying(255),
    cep character varying(255),
    cidade character varying(255),
    complemento character varying(255),
    logradouro character varying(255),
    numero character varying(255),
    "país" character varying(255),
    uf character varying(255),
    CONSTRAINT addresses_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.business_area (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT business_area_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.hobbies (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT hobbies_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.preferences (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT preferences_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.professions (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT professions_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.revenue_ranges (
    id character varying(255) NOT NULL,
    label character varying(255) NOT NULL,
    max_value numeric(19,2),
    min_value numeric(19,2),
    CONSTRAINT revenue_ranges_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.salon (
    id bigint NOT NULL,
    CONSTRAINT salon_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.users (
    id character varying(255) NOT NULL,
    active boolean NOT NULL,
    avatar character varying(255) NOT NULL,
    birth_date date NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    plan character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    adress_id character varying(255),
    business_area_id character varying(255),
    salon_id bigint,
    profession_id character varying(255),
    revenue_range_id character varying(255),
    marketing_opt_in boolean DEFAULT false,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT ukbukhmib9h8hful9l9qevja2ce UNIQUE (adress_id),
    CONSTRAINT users_plan_check CHECK (((plan)::text = ANY ((ARRAY[
        'GOLD'::character varying,
        'BLACK'::character varying,
        'INFINITE'::character varying,
        'ADMIN'::character varying,
        'BARMAN'::character varying,
        'CONCIERGE'::character varying
    ])::text[]))),
    CONSTRAINT fk532xjpnu4dw0rpxdvodistgdx FOREIGN KEY (profession_id) REFERENCES public.professions(id),
    CONSTRAINT fkcn0pyhx47yvwf0nrpudmu3art FOREIGN KEY (business_area_id) REFERENCES public.business_area(id),
    CONSTRAINT fkjgx3b740tbfrew3se4jttd9f5 FOREIGN KEY (adress_id) REFERENCES public.addresses(id),
    CONSTRAINT fkjy07yquaqfik5ki9dwcj6ch7t FOREIGN KEY (salon_id) REFERENCES public.salon(id),
    CONSTRAINT fkjy2bg77dea357khk2dvuynuer FOREIGN KEY (revenue_range_id) REFERENCES public.revenue_ranges(id)
);

CREATE TABLE IF NOT EXISTS public.checkins (
    id character varying(255) NOT NULL,
    status character varying(255),
    "timestamp" timestamp(6) without time zone NOT NULL,
    user_id character varying(255),
    CONSTRAINT checkins_pkey PRIMARY KEY (id),
    CONSTRAINT checkins_status_check CHECK (((status)::text = ANY ((ARRAY[
        'ARRIVED'::character varying,
        'SERVED'::character varying,
        'FINISHED'::character varying
    ])::text[]))),
    CONSTRAINT fk81gcnk1emrj19gv3x67u4f8bx FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS public.events (
    id character varying(255) NOT NULL,
    active boolean NOT NULL,
    available_spots integer NOT NULL,
    capacity integer NOT NULL,
    category character varying(255) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    date_time timestamp(6) without time zone NOT NULL,
    description text NOT NULL,
    image character varying(255) NOT NULL,
    location character varying(255) NOT NULL,
    speaker character varying(255) NOT NULL,
    title character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    CONSTRAINT events_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.event_allowed_plans (
    event_id character varying(255) NOT NULL,
    plan_name character varying(255) NOT NULL,
    CONSTRAINT event_allowed_plans_pkey PRIMARY KEY (event_id, plan_name),
    CONSTRAINT event_allowed_plans_plan_name_check CHECK (((plan_name)::text = ANY ((ARRAY[
        'GOLD'::character varying,
        'BLACK'::character varying,
        'INFINITE'::character varying,
        'BARMAN'::character varying,
        'CONCIERGE'::character varying,
        'ADMIN'::character varying,
        'SUPERADMIN'::character varying
    ])::text[]))),
    CONSTRAINT fk9fad5ipud2lm540b6tf7cd7ve FOREIGN KEY (event_id) REFERENCES public.events(id)
);

CREATE TABLE IF NOT EXISTS public.registrations (
    id character varying(255) NOT NULL,
    registration_date timestamp(6) without time zone NOT NULL,
    status character varying(255) NOT NULL,
    event_id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    CONSTRAINT registrations_pkey PRIMARY KEY (id),
    CONSTRAINT registrations_status_check CHECK (((status)::text = ANY ((ARRAY[
        'REGISTERED'::character varying,
        'CANCELLED'::character varying,
        'ATTENDED'::character varying
    ])::text[]))),
    CONSTRAINT fk8mi58jt1s8fxmi56jnau0cxqw FOREIGN KEY (event_id) REFERENCES public.events(id),
    CONSTRAINT fkl2iby9n9hp8jwkfj8i96pkxpi FOREIGN KEY (user_id) REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS public.user_hobbies (
    user_id character varying(255) NOT NULL,
    hobby_id character varying(255) NOT NULL,
    CONSTRAINT user_hobbies_pkey PRIMARY KEY (user_id, hobby_id),
    CONSTRAINT fk1aia57pgihndujoa8vm7eoccn FOREIGN KEY (user_id) REFERENCES public.users(id),
    CONSTRAINT fkp0lyrwcjffaldpgw3iinwqbj0 FOREIGN KEY (hobby_id) REFERENCES public.hobbies(id)
);

CREATE TABLE IF NOT EXISTS public.user_preferences (
    user_id character varying(255) NOT NULL,
    preference_id character varying(255) NOT NULL,
    CONSTRAINT user_preferences_pkey PRIMARY KEY (user_id, preference_id),
    CONSTRAINT fkepakpib0qnm82vmaiismkqf88 FOREIGN KEY (user_id) REFERENCES public.users(id),
    CONSTRAINT fkb4v7lkbkok3n266e48xt886vy FOREIGN KEY (preference_id) REFERENCES public.preferences(id)
);

CREATE TABLE IF NOT EXISTS public.visitas (
    id character varying(255) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    salon_id bigint,
    user_id character varying(255),
    CONSTRAINT visitas_pkey PRIMARY KEY (id),
    CONSTRAINT fk6cpepuigxm9w8dy8a9hi3uq0e FOREIGN KEY (salon_id) REFERENCES public.salon(id),
    CONSTRAINT fkumr6sv929n9yyicceqv1cr49 FOREIGN KEY (user_id) REFERENCES public.users(id)
);
