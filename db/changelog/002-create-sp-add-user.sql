CREATE OR REPLACE PROCEDURE get_related_tasks (
    p_user_id IN NUMBER,
    p_result OUT SYS_REFCURSOR
) AS
BEGIN
OPEN p_result FOR
SELECT t.*,
       CASE WHEN wc.min_ln = 1 THEN 0 ELSE 1 END AS vaitro
FROM task t
         JOIN (
    -- chỉ những dòng thuộc prefix liên tục (level_number = rn) và user = p_user_id
    SELECT x.workflow_id,
           MIN(x.level_number) AS min_ln
    FROM (
             SELECT wl.workflow_id,
                    wl.user_id,
                    wl.level_number,
                    ROW_NUMBER() OVER (PARTITION BY wl.workflow_id ORDER BY wl.level_number) AS rn
             FROM workflow_level wl
         ) x
    WHERE x.level_number = x.rn
      AND x.user_id = p_user_id
    GROUP BY x.workflow_id
) wc
              ON wc.workflow_id = t.workflow_id;
END;