-- ============================================================
-- V3__catalog_schema.sql
-- Urban Services Platform — Phase 2: Catalog Schema
-- Database: MySQL 8.x
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- ============================================================
-- CATEGORIES
-- Top-level catalog grouping (e.g., Cleaning, Repair).
-- ============================================================
CREATE TABLE IF NOT EXISTS categories (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255)        NOT NULL,
    slug            VARCHAR(255)        NOT NULL COMMENT 'URL-friendly unique identifier',
    description     TEXT,
    icon_url        VARCHAR(1000)                COMMENT 'URL to category icon/image',
    is_active       TINYINT(1)          NOT NULL DEFAULT 1,
    sort_order      INT                 NOT NULL DEFAULT 0,
    
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_categories_slug (slug),
    INDEX       idx_categories_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- SUBCATEGORIES
-- Second-level grouping under Categories.
-- ============================================================
CREATE TABLE IF NOT EXISTS subcategories (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    category_id     BIGINT UNSIGNED     NOT NULL,
    name            VARCHAR(255)        NOT NULL,
    slug            VARCHAR(255)        NOT NULL,
    description     TEXT,
    image_url       VARCHAR(1000),
    is_active       TINYINT(1)          NOT NULL DEFAULT 1,
    sort_order      INT                 NOT NULL DEFAULT 0,
    
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_subcategories_slug (slug),
    CONSTRAINT  fk_subcategories_cat  FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    INDEX       idx_subcategories_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- SERVICES
-- The actual bookable items/jobs.
-- ============================================================
CREATE TABLE IF NOT EXISTS services (
    id                         BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    subcategory_id             BIGINT UNSIGNED     NOT NULL,
    name                       VARCHAR(255)        NOT NULL,
    slug                       VARCHAR(255)        NOT NULL,
    description                TEXT,
    detailed_description       TEXT,
    base_price                 DECIMAL(10, 2)      NOT NULL,
    estimated_duration_minutes INT UNSIGNED        NOT NULL,
    image_url                  VARCHAR(1000),
    is_active                  TINYINT(1)          NOT NULL DEFAULT 1,
    sort_order                 INT                 NOT NULL DEFAULT 0,
    
    created_at                 DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at                 DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_services_slug     (slug),
    CONSTRAINT  fk_services_subcat   FOREIGN KEY (subcategory_id) REFERENCES subcategories(id) ON DELETE CASCADE,
    INDEX       idx_services_active  (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
