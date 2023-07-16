DELETE FROM forum;
DELETE FROM forum_category;
DROP TABLE post_f CASCADE;


INSERT INTO forum_category (name) VALUES ('General');

INSERT INTO forum (id, category_id, name)
VALUES ('f', (SELECT id FROM forum_category WHERE name = 'General'), 'Flooding');

CREATE TABLE IF NOT EXISTS post_f (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT DEFAULT NULL REFERENCES account(id),
    parent_id BIGINT DEFAULT NULL REFERENCES post_f(id) ON DELETE CASCADE,
    publish_time TIMESTAMP NOT NULL DEFAULT NOW(),
    topic TEXT NOT NULL DEFAULT '',
    text TEXT NOT NULL
);
