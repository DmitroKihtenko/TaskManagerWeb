SELECT id,name,enabled,metadata
FROM (
    SELECT *
    FROM (
         SELECT rownum num,id,name,enabled,metadata
         FROM user_data
         ORDER BY name
    )
    WHERE num<=?
    )
WHERE num>?

