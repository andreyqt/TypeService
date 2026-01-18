ALTER TABLE leaderboards_60s
    DROP CONSTRAINT pk_lbs_60s;
ALTER TABLE leaderboards_60s
    ADD CONSTRAINT pk_lbs_60s PRIMARY KEY (rank);