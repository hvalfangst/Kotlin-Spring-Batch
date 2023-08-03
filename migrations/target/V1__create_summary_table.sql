-- tables/migration/target/V1__create_summary_table.sql

CREATE TABLE summary (
                         id SERIAL PRIMARY KEY,
                         total_sales DOUBLE PRECISION,
                         date DATE
);
