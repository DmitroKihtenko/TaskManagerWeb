GRANT UNLIMITED TABLESPACE TO &schema;

CREATE table accounting_log
(
    id          INTEGER NOT NULL,
    date_time   TIMESTAMP NOT NULL,
    table_name  VARCHAR2(30) NOT NULL,
    event       VARCHAR2(50) NOT NULL,
    object      INTEGER NOT NULL,
    author      VARCHAR2(30) NOT NULL
);

ALTER table accounting_log ADD CONSTRAINT accounting_log_pk PRIMARY KEY ( id );

CREATE TABLE &schema..task (
    id         INTEGER NOT NULL,
    title      NVARCHAR2(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date   DATE,
    interval   NUMERIC(13),
    active     CHAR(1) NOT NULL,
    user_data  INTEGER NOT NULL
);

ALTER TABLE &schema..task ADD CONSTRAINT task_pk PRIMARY KEY ( id );

ALTER TABLE &schema..task ADD CONSTRAINT user_task_title_unique UNIQUE ( title, user_data );

CREATE TABLE &schema..user_data (
    id         INTEGER NOT NULL,
    name       NVARCHAR2(24) NOT NULL,
    password   NVARCHAR2(64) NOT NULL,
    enabled    CHAR(1) NOT NULL,
    metadata   RAW(1000) NOT NULL
);

ALTER TABLE &schema..user_data ADD CONSTRAINT user_pk PRIMARY KEY ( id );

ALTER TABLE &schema..user_data ADD CONSTRAINT user_name_unique UNIQUE ( name );

ALTER TABLE &schema..task
    ADD CONSTRAINT task_user_fk FOREIGN KEY ( user_data )
        REFERENCES &schema..user_data ( id )
        ON DELETE CASCADE;

CREATE SEQUENCE accounting_log_sequence
    START WITH 0
    MINVALUE 0
    INCREMENT BY 1
    NOCYCLE;

CREATE SEQUENCE &schema..task_sequence
    START WITH 0
    MINVALUE 0
    INCREMENT BY 1
    NOCYCLE;

CREATE SEQUENCE &schema..user_sequence
    START WITH 0
    MINVALUE 0
    INCREMENT BY 1
    NOCYCLE;
