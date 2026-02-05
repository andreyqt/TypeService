CREATE TABLE IF NOT EXISTS personal_bests
(
    language VARCHAR(255) NOT NULL,
    mode VARCHAR(255) NOT NULL,
    mode2 VARCHAR(255) NOT NULL,
    wpm DOUBLE PRECISION,
    acc DOUBLE PRECISION,
    consistency DOUBLE PRECISION,
    punctuation BOOLEAN,
    numbers BOOLEAN,
    timestamp BIGINT,

    CONSTRAINT pk_lang_mode_mode2 PRIMARY KEY(language, mode, mode2, punctuation, numbers)
);