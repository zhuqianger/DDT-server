CREATE TABLE IF NOT EXISTS `daily_login_record` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `player_id` BIGINT UNSIGNED NOT NULL COMMENT '玩家ID，关联player.id',
  `sign_in_days` INT NOT NULL DEFAULT 0 COMMENT '当前周期累计签到天数（例如当月）',
  `last_sign_in_date` DATE DEFAULT NULL COMMENT '最后一次签到日期',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_player_once` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='玩家每日签到状态表（每人一条，按月清空）';
