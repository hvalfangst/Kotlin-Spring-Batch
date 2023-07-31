-- db/migration/target/V1__create_summary_table.sql

CREATE TABLE summary (
                         id SERIAL PRIMARY KEY,
                         totalSales DOUBLE PRECISION,
                         date DATE
);
