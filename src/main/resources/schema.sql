DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;

CREATE TABLE book
(
    isbn              LONG PRIMARY KEY,
    author_name       VARCHAR(50),
    title             VARCHAR(50),
    release           VARCHAR(8),
    date_publication  TIMESTAMP(6) NULL DEFAULT NULL
);



