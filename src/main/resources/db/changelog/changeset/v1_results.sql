CREATE TABLE IF NOT EXISTS results
(
    id VARCHAR(255) PRIMARY KEY,
    uid VARCHAR(255),
    wpm DOUBLE PRECISION,
    char_stats INTEGER[4],
    acc DOUBLE PRECISION,
    mode VARCHAR(255),
    mode2 VARCHAR(255),
    timestamp BIGINT,
    test_duration DOUBLE PRECISION
);

CREATE INDEX IF NOT EXISTS idx_results_timestamp ON results(timestamp);