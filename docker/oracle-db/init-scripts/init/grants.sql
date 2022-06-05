GRANT CREATE SESSION TO &user_name;

GRANT SELECT, UPDATE, INSERT, DELETE ON &schema..task TO &user_name;
GRANT SELECT, UPDATE, INSERT, DELETE ON &schema..user_data TO &user_name;

GRANT SELECT ON &schema..task_sequence TO &user_name;
GRANT SELECT ON &schema..user_sequence TO &user_name;
