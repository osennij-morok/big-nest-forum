CREATE TABLE IF NOT EXISTS forum_category (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS forum (
    id VARCHAR PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES forum_category(id) ON DELETE NO ACTION,
    name VARCHAR NOT NULL UNIQUE
);

-- DO $$ BEGIN
-- CREATE TYPE account_role AS ENUM ('ADMIN', 'MODER');
-- EXCEPTION
--     WHEN duplicate_object THEN null;
-- END $$;

-- DO '
-- DECLARE
-- BEGIN
--     CREATE TYPE account_role AS ENUM ('ADMIN', 'MODER');
--     EXCEPTION
--         WHEN duplicate_object THEN null;
-- END;
-- ' LANGUAGE PLPGSQL;

-- CREATE TYPE account_role AS ENUM ('ADMIN', 'MODER');
-- EXCEPTION
--     WHEN duplicate_object THEN null;

CREATE TABLE IF NOT EXISTS account (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR NOT NULL UNIQUE,
    password_hash VARCHAR NOT NULL,
    role account_role NOT NULL DEFAULT 'USER',
    blocked BOOLEAN NOT NULL DEFAULT FALSE
);