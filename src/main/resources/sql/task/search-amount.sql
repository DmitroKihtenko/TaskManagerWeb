SELECT COUNT(id) amount
FROM task
WHERE REGEXP_LIKE(title,?) AND user_data=?
