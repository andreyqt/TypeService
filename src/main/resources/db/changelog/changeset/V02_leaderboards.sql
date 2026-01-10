CREATE TABLE IF NOT EXISTS leaderboards_english_60s
(
    rank INTEGER,
    uid VARCHAR(255) PRIMARY KEY,
    wpm DOUBLE PRECISION,
    raw DOUBLE PRECISION,
    acc DOUBLE PRECISION,
    timestamp BIGINT,
    consistency DOUBLE PRECISION,
    name VARCHAR(255),
    discord_id VARCHAR(255),
    discord_avatar VARCHAR(255)
);

CREATE INDEX wmp_idx ON leaderboards_english_60s(wpm DESC);