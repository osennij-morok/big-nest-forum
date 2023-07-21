CREATE TYPE account_role AS ENUM ('USER', 'MODER', 'ADMIN', 'OWNER');

CREATE TABLE IF NOT EXISTS account (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR NOT NULL UNIQUE,
    password_hash VARCHAR NOT NULL,
    role account_role NOT NULL DEFAULT 'USER',
    blocked BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO account (username, password_hash, role)
VALUES (
    'admin', 
    '$argon2id$v=19$m=16384,t=2,p=1$BzRgPR9Mw6MB85p3w1fwIA$E4ZP9TFUNeCHuSmZn/ZNfo8hViU7SuilSu+Z//QYeog', 
    'OWNER'
);
