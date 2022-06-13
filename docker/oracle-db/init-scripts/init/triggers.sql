CREATE OR REPLACE TRIGGER user_accounting
    AFTER
        INSERT OR
        UPDATE OR
        DELETE
    ON &schema..user_data
    FOR EACH ROW
DECLARE
    v_username VARCHAR2(30);
BEGIN
    SELECT user
    INTO v_username
    FROM dual;
    CASE
        WHEN INSERTING THEN
            INSERT INTO accounting_log(id,date_time,table_name,event,object,author)
            VALUES (accounting_log_sequence.NEXTVAL,CURRENT_TIMESTAMP,'user','insert',:new.id,v_username);
        WHEN UPDATING THEN
            INSERT INTO accounting_log(id,date_time,table_name,event,object,author)
            VALUES (accounting_log_sequence.NEXTVAL,CURRENT_TIMESTAMP,'user','update',:new.id,v_username);
        WHEN DELETING THEN
            INSERT INTO accounting_log(id,date_time,table_name,event,object,author)
            VALUES (accounting_log_sequence.NEXTVAL,CURRENT_TIMESTAMP,'user','delete',:old.id,v_username);
    END CASE;
END;
/

CREATE OR REPLACE TRIGGER task_accounting
    AFTER
        INSERT OR
        UPDATE OR
        DELETE
    ON &schema..task
    FOR EACH ROW
DECLARE
    v_username varchar2(30);
BEGIN
    SELECT user
    INTO v_username
    FROM dual;
    CASE
        WHEN INSERTING THEN
            INSERT INTO accounting_log(id,date_time,table_name,event,object,author)
            VALUES (accounting_log_sequence.NEXTVAL,CURRENT_TIMESTAMP,'task','insert',:new.id,v_username);
        WHEN UPDATING THEN
            INSERT INTO accounting_log(id,date_time,table_name,event, object,author)
            VALUES (accounting_log_sequence.NEXTVAL,CURRENT_TIMESTAMP,'task','update',:new.id,v_username);
        WHEN DELETING THEN
            INSERT INTO accounting_log(id,date_time,table_name,event,object,author)
            VALUES (accounting_log_sequence.NEXTVAL,CURRENT_TIMESTAMP,'task','delete',:old.id,v_username);
    END CASE;
END;
/
