-- tables/migration/source/V1__create_sales_table.sql

CREATE TABLE sales (
                       id SERIAL PRIMARY KEY,
                       product VARCHAR(100),
                       quantity INTEGER,
                       price DOUBLE PRECISION,
                       date DATE
);
