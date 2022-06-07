SELECT id,title,start_date,end_date,interval,active
FROM (
    SELECT *
    FROM (
         SELECT rownum num,id,title,start_date,end_date,interval,active
         FROM task
         WHERE REGEXP_LIKE(title,?) AND user_data=?
         ORDER BY title
         )
    WHERE num<=?
    )
WHERE num>?
