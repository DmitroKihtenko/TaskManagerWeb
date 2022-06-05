SELECT COUNT(id) amount
FROM user_data
WHERE REGEXP_LIKE(name,?)
