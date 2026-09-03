-- ============================================================
-- V1__initial_schema.sql
-- Urban Services Platform — Phase 1: Foundation Schema
-- Database: MySQL 8.x
-- Engine:   InnoDB
-- Charset:  utf8mb4_unicode_ci
--
-- This migration creates only the cross-cutting foundation tables.
-- Business domain tables (users, customers, categories, bookings, etc.)
-- are created in their respective phase migrations.
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- ============================================================
-- AUDIT LOGS
-- Central audit trail used by ALL modules.
-- Records security-relevant and business-critical events.
-- Never store passwords, tokens, or PII in this table.
-- ============================================================
CREATE TABLE IF NOT EXISTS audit_logs (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    event_type      VARCHAR(100)        NOT NULL COMMENT 'e.g. USER_REGISTERED, LOGIN_SUCCESS, ADDRESS_CREATED',
    entity_type     VARCHAR(100)                 COMMENT 'e.g. USER, CUSTOMER, BOOKING',
    entity_id       BIGINT UNSIGNED              COMMENT 'PK of the affected entity',
    actor_type      VARCHAR(50)                  COMMENT 'CUSTOMER, PROVIDER, ADMIN, SYSTEM',
    actor_id        BIGINT UNSIGNED              COMMENT 'PK of user who performed the action',
    ip_address      VARCHAR(45)                  COMMENT 'IPv4 or IPv6 — max 45 chars for IPv6',
    user_agent      VARCHAR(500)                 COMMENT 'HTTP User-Agent header',
    description     TEXT                         COMMENT 'Human-readable summary of the event',
    meta_data       JSON                         COMMENT 'Additional structured context (no sensitive data)',
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    INDEX idx_audit_event_type      (event_type),
    INDEX idx_audit_entity          (entity_type, entity_id),
    INDEX idx_audit_actor           (actor_type, actor_id),
    INDEX idx_audit_created_at      (created_at)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Central audit log — all security and business events';


-- ============================================================
-- MEDIA FILES
-- Tracks all uploaded media assets (S3 objects).
-- Referenced by categories, services, providers, customers, banners.
-- The actual binary is stored in AWS S3 — only the reference here.
-- ============================================================
CREATE TABLE IF NOT EXISTS media_files (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    public_id       CHAR(36)            NOT NULL COMMENT 'UUID — safe to expose in APIs',
    entity_type     VARCHAR(100)        NOT NULL COMMENT 'CATEGORY, SERVICE, PROVIDER, CUSTOMER, BANNER',
    entity_id       BIGINT UNSIGNED              COMMENT 'FK to the owning entity (optional at upload time)',
    file_name       VARCHAR(500)        NOT NULL COMMENT 'Original file name',
    s3_key          VARCHAR(1000)       NOT NULL COMMENT 'Full S3 object key',
    s3_bucket       VARCHAR(255)        NOT NULL COMMENT 'S3 bucket name',
    content_type    VARCHAR(100)        NOT NULL COMMENT 'MIME type e.g. image/jpeg',
    file_size_bytes BIGINT UNSIGNED              COMMENT 'File size in bytes',
    cdn_url         VARCHAR(2000)                COMMENT 'CloudFront distribution URL',
    status          VARCHAR(30)         NOT NULL DEFAULT 'PENDING'
                        COMMENT 'PENDING | UPLOADED | DELETED',
    uploaded_by     BIGINT UNSIGNED              COMMENT 'User who uploaded',
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_media_public_id     (public_id),
    UNIQUE KEY  uk_media_s3_key        (s3_key(255)),
    INDEX       idx_media_entity       (entity_type, entity_id),
    INDEX       idx_media_status       (status),
    INDEX       idx_media_created_at   (created_at)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='All uploaded media asset references — binary stored in S3';


-- ============================================================
-- APP CONFIG
-- Dynamic application configuration stored in the database.
-- Allows changing non-sensitive settings without redeployment.
-- ============================================================
CREATE TABLE IF NOT EXISTS app_config (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    config_key      VARCHAR(200)        NOT NULL COMMENT 'Unique config key e.g. PLATFORM_COMMISSION_PERCENT',
    config_value    TEXT                NOT NULL COMMENT 'String-encoded value',
    value_type      VARCHAR(30)         NOT NULL DEFAULT 'STRING'
                        COMMENT 'STRING | INTEGER | DECIMAL | BOOLEAN | JSON',
    description     VARCHAR(500)                 COMMENT 'Human-readable description',
    is_sensitive    TINYINT(1)          NOT NULL DEFAULT 0
                        COMMENT '1 = value should not be returned in public APIs',
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_config_key          (config_key)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Dynamic application configuration';


-- ============================================================
-- SEED: default app config
-- ============================================================
INSERT IGNORE INTO app_config (config_key, config_value, value_type, description, is_sensitive)
VALUES
    ('PLATFORM_COMMISSION_PERCENT',     '15',    'DECIMAL',  'Platform commission % on each booking',        0),
    ('MAX_ADDRESSES_PER_CUSTOMER',      '10',    'INTEGER',  'Maximum saved addresses per customer',         0),
    ('BOOKING_CANCELLATION_WINDOW_HRS', '2',     'INTEGER',  'Hours before appointment to allow free cancel',0),
    ('S3_PRESIGNED_URL_EXPIRY_MINUTES', '15',    'INTEGER',  'Pre-signed S3 URL expiry in minutes',          0),
    ('PLATFORM_CURRENCY',               'INR',   'STRING',   'Platform default currency code',               0),
    ('PLATFORM_COUNTRY_CODE',           'IN',    'STRING',   'Platform default country ISO code',            0),
    ('SUPPORT_PHONE',                   '',      'STRING',   'Customer support phone number',                0),
    ('SUPPORT_EMAIL',                   '',      'STRING',   'Customer support email',                       0);


SET FOREIGN_KEY_CHECKS = 1;
