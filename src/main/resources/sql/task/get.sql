SELECT id,title,start_date,end_date,interval,active
FROM task
WHERE id=? AND user_data=?
