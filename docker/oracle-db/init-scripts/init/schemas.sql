CREATE TABLESPACE &space
    DATAFILE &space_file
    SIZE 1G;

CREATE TEMPORARY TABLESPACE &temp_space
    TEMPFILE &temp_space_file
    SIZE 1G;

CREATE USER &user_name
    IDENTIFIED BY &user_password
    PROFILE &tm_profile
    DEFAULT TABLESPACE &space
    TEMPORARY TABLESPACE &temp_space;
