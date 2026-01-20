CREATE TABLE IF NOT EXISTS leaderboards_60s
(
    rank INTEGER,
    name VARCHAR(255) NOT NULL,
    uid VARCHAR(255) NOT NULL,
    wpm DOUBLE PRECISION,
    acc DOUBLE PRECISION,
    timestamp BIGINT,
    discord_id VARCHAR(255),
    discord_avatar VARCHAR(255)
);