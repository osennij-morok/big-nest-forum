DELETE FROM forum;
DELETE FROM forum_category;

INSERT INTO forum_category (name) VALUES ('general');

INSERT INTO forum (id, category_id, name)
VALUES
    ('f',
     (SELECT id FROM forum_category WHERE name = 'general'),
--      1,
     'Flooding'),
    ('vg',
     (SELECT id FROM forum_category WHERE name = 'general'),
--      1,
     'Video-games');

DROP TABLE IF EXISTS post_f;
DROP TABLE IF EXISTS post_vg;

CREATE TABLE IF NOT EXISTS post_f (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT DEFAULT NULL REFERENCES account(id),
    parent_id BIGINT DEFAULT NULL REFERENCES post_f(id) ON DELETE CASCADE,
    publish_time TIMESTAMP NOT NULL DEFAULT NOW(),
    topic TEXT NOT NULL DEFAULT '',
    text TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS post_vg (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT DEFAULT NULL REFERENCES account(id),
    parent_id BIGINT DEFAULT NULL REFERENCES post_f(id) ON DELETE CASCADE,
    publish_time TIMESTAMP NOT NULL DEFAULT NOW(),
    topic TEXT NOT NULL DEFAULT '',
    text TEXT NOT NULL
);
