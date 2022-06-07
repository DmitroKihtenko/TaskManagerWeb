SELECT id,title,start_date,end_date,interval,active
FROM task
WHERE ((end_date IS NULL AND start_date>=? AND start_date<=?) OR
    (end_date IS NOT NULL AND (NOT (start_date<? AND end_date<?)
   OR NOT (start_date>? AND end_date>?)))) AND user_data=?
