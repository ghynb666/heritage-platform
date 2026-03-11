USE heritage_platform;

ALTER TABLE announcement
ADD COLUMN IF NOT EXISTS sort int DEFAULT 0 COMMENT '排序' AFTER status;

SET @has_idx_sort = (
  SELECT COUNT(*)
  FROM information_schema.statistics
  WHERE table_schema = DATABASE()
    AND table_name = 'announcement'
    AND index_name = 'idx_sort'
);

SET @create_idx_sort = IF(
  @has_idx_sort = 0,
  'CREATE INDEX idx_sort ON announcement (sort)',
  'SELECT 1'
);

PREPARE stmt FROM @create_idx_sort;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
