INSERT INTO forum_category (name) VALUES ('General')
ON CONFLICT DO NOTHING;

INSERT INTO forum (id, category_id, name)
VALUES ('f', (SELECT id FROM forum_category WHERE name = 'General'), 'Flooding')
ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS post_f (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    sender_id BIGINT DEFAULT NULL REFERENCES account(id),
    parent_id BIGINT DEFAULT NULL REFERENCES post_f(id) ON DELETE CASCADE,
    publish_time TIMESTAMP NOT NULL DEFAULT NOW(),
    topic TEXT NOT NULL DEFAULT '',
    text TEXT NOT NULL
);

INSERT INTO post_f (parent_id, topic, text)
SELECT
    NULL, 'Greeting', 'Lets greet each other'
WHERE NOT EXISTS (
    SELECT 1 FROM post_f WHERE topic = 'Greeting'
);

INSERT INTO post_f (parent_id, text)
SELECT
    (SELECT id FROM post_f WHERE topic = 'Greeting'),
    'Hello, guys!'
WHERE NOT EXISTS (
    SELECT 1 FROM post_f WHERE text = 'Hello, guys!'
);

INSERT INTO post_f (parent_id, topic, text)
SELECT
    NULL, 'Dogs and cats', 'Lets discus dogs and cats'
WHERE NOT EXISTS (
    SELECT 1 FROM post_f WHERE topic = 'Dogs and cats'
);
