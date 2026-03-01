CREATE TABLE daily_average_result
(
    date         DATE PRIMARY KEY,
    average_wmp  DOUBLE PRECISION NOT NULL,
    average_acc  DOUBLE PRECISION NOT NULL,
    num_of_tests INTEGER          NOT NULL,
    time         DOUBLE PRECISION NOT NULL
);