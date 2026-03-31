CREATE DATABASE IF NOT EXISTS `apisentinel` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `apisentinel`;

-- 监控接口配置表
CREATE TABLE IF NOT EXISTS `monitor_api` (
  `id`                BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name`              VARCHAR(100) NOT NULL COMMENT '接口名称',
  `url`               VARCHAR(500) NOT NULL COMMENT '接口URL',
  `method`            VARCHAR(10)  NOT NULL DEFAULT 'GET' COMMENT '请求方法 GET/POST/PUT/DELETE/HEAD',
  `headers`           TEXT         COMMENT '请求头 JSON格式',
  `body`              TEXT         COMMENT '请求体 JSON格式',
  `check_interval`    INT          NOT NULL DEFAULT 60 COMMENT '检查间隔(秒)',
  `timeout`           INT          NOT NULL DEFAULT 10 COMMENT '超时时间(秒)',
  `alert_threshold`   INT          NOT NULL DEFAULT 3 COMMENT '连续失败N次后告警',
  `ding_webhook`      VARCHAR(500) COMMENT '钉钉Webhook地址',
  `enabled`           TINYINT      NOT NULL DEFAULT 1 COMMENT '是否启用 0-禁用 1-启用',
  `last_status`       TINYINT      COMMENT '最后检查状态 0-失败 1-成功',
  `last_status_code`  INT          COMMENT '最后响应状态码',
  `last_response_time` INT         COMMENT '最后响应时间(ms)',
  `last_check_time`   DATETIME     COMMENT '最后检查时间',
  `consecutive_fails` INT          NOT NULL DEFAULT 0 COMMENT '连续失败次数',
  `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监控接口配置表';

-- 检查记录表
CREATE TABLE IF NOT EXISTS `check_record` (
  `id`            BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `api_id`        BIGINT    NOT NULL COMMENT '关联接口ID',
  `success`       TINYINT   NOT NULL COMMENT '是否成功 0-失败 1-成功',
  `status_code`   INT       COMMENT 'HTTP状态码',
  `response_time` INT       COMMENT '响应时间(ms)',
  `error_msg`     VARCHAR(500) COMMENT '错误信息',
  `check_time`    DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检查时间',
  PRIMARY KEY (`id`),
  INDEX `idx_api_id` (`api_id`),
  INDEX `idx_check_time` (`check_time`),
  INDEX `idx_api_check_time` (`api_id`, `check_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检查记录表';

-- 示例数据
INSERT INTO `monitor_api` (`name`, `url`, `method`, `check_interval`, `timeout`, `alert_threshold`, `enabled`)
VALUES
  ('百度首页', 'https://www.baidu.com', 'GET', 60, 10, 3, 1),
  ('GitHub API', 'https://api.github.com', 'GET', 120, 15, 3, 1);
