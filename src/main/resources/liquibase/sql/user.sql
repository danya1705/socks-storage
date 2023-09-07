--liquibase formatted sql

--changeset dKarpov:1
CREATE TABLE IF NOT EXISTS socks
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    color       TEXT,
    cotton_part INTEGER,
    quantity    INTEGER
);
