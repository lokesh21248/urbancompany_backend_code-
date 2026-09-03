-- ============================================================
-- V2__business_schema.sql
-- Urban Services Platform — Phase 2: Business Schema
-- Database: MySQL 8.x
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- ============================================================
-- USERS
-- Central table for authentication and core identity.
-- Applies to Customers, Providers, and Admins.
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    firebase_uid    VARCHAR(128)                 COMMENT 'Populated in Phase 27 (Firebase Auth)',
    phone_number    VARCHAR(20)         NOT NULL COMMENT 'E.164 format e.g., +919876543210',
    email           VARCHAR(255)                 COMMENT 'Optional for customers, required for providers/admins',
    full_name       VARCHAR(255)        NOT NULL,
    role            VARCHAR(30)         NOT NULL COMMENT 'CUSTOMER | PROVIDER | ADMIN',
    status          VARCHAR(30)         NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE | INACTIVE | BANNED',
    
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_users_firebase_uid (firebase_uid),
    UNIQUE KEY  uk_users_phone        (phone_number),
    UNIQUE KEY  uk_users_email        (email),
    INDEX       idx_users_role        (role),
    INDEX       idx_users_status      (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- CUSTOMER SAVED ADDRESSES
-- Multiple saved locations for a customer.
-- ============================================================
CREATE TABLE IF NOT EXISTS addresses (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    user_id         BIGINT UNSIGNED     NOT NULL COMMENT 'FK to users.id',
    address_line_1  VARCHAR(500)        NOT NULL,
    address_line_2  VARCHAR(500)                 COMMENT 'Apartment, suite, etc.',
    city            VARCHAR(100)        NOT NULL,
    state           VARCHAR(100)        NOT NULL,
    postal_code     VARCHAR(20)         NOT NULL,
    country         VARCHAR(100)        NOT NULL DEFAULT 'India',
    latitude        DECIMAL(10, 7)               COMMENT 'Cached coordinates for distance sorting',
    longitude       DECIMAL(10, 7)               COMMENT 'Cached coordinates for distance sorting',
    label           VARCHAR(50)         NOT NULL DEFAULT 'OTHER' COMMENT 'HOME | WORK | OTHER',
    is_default      TINYINT(1)          NOT NULL DEFAULT 0,
    
    created_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at      DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    CONSTRAINT  fk_addresses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX       idx_addresses_city (city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- PROVIDER PROFILES
-- Extended data specifically for users with role=PROVIDER.
-- ============================================================
CREATE TABLE IF NOT EXISTS provider_profiles (
    id                    BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    user_id               BIGINT UNSIGNED     NOT NULL COMMENT 'FK to users.id',
    business_name         VARCHAR(255)                 COMMENT 'Optional trading name',
    bio                   TEXT,
    rating                DECIMAL(3, 2)       NOT NULL DEFAULT 0.00,
    total_reviews         INT UNSIGNED        NOT NULL DEFAULT 0,
    verification_status   VARCHAR(30)         NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING | VERIFIED | REJECTED',
    is_online             TINYINT(1)          NOT NULL DEFAULT 0 COMMENT 'Toggle for accepting new instant jobs',
    latitude              DECIMAL(10, 7)               COMMENT 'Current or base location',
    longitude             DECIMAL(10, 7)               COMMENT 'Current or base location',
    
    created_at            DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at            DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_provider_user (user_id),
    CONSTRAINT  fk_provider_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX       idx_provider_status (verification_status),
    INDEX       idx_provider_online (is_online)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
