-- ============================================================
-- V7__banners_schema.sql
-- Urban Services Platform
-- Database: MySQL 8.x
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS banners (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    title           VARCHAR(255)        NOT NULL,
    image_url       VARCHAR(1000)       NOT NULL,
    link_url        VARCHAR(1000),
    is_active       TINYINT(1)          NOT NULL DEFAULT 1,
    sort_order      INT                 NOT NULL DEFAULT 0,
    
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    INDEX       idx_banners_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
