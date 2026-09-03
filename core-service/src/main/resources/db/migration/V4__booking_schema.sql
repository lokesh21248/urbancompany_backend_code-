-- ============================================================
-- V4__booking_schema.sql
-- Urban Services Platform — Phase 2: Booking Schema
-- Database: MySQL 8.x
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- ============================================================
-- BOOKINGS
-- The core transaction mapping a customer, a provider, and a schedule.
-- ============================================================
CREATE TABLE IF NOT EXISTS bookings (
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    customer_id         BIGINT UNSIGNED     NOT NULL COMMENT 'FK to users.id',
    provider_id         BIGINT UNSIGNED              COMMENT 'FK to users.id. Null until assigned.',
    address_id          BIGINT UNSIGNED     NOT NULL COMMENT 'FK to addresses.id',
    status              VARCHAR(30)         NOT NULL DEFAULT 'PENDING' 
                            COMMENT 'PENDING | ACCEPTED | IN_PROGRESS | COMPLETED | CANCELLED',
    scheduled_time      DATETIME            NOT NULL COMMENT 'When the job should happen',
    
    total_amount        DECIMAL(10, 2)      NOT NULL DEFAULT 0.00,
    discount_amount     DECIMAL(10, 2)      NOT NULL DEFAULT 0.00,
    final_amount        DECIMAL(10, 2)      NOT NULL DEFAULT 0.00,
    
    cancellation_reason TEXT,
    
    created_at          DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at          DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    CONSTRAINT  fk_bookings_customer FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT  fk_bookings_provider FOREIGN KEY (provider_id) REFERENCES users(id),
    CONSTRAINT  fk_bookings_address  FOREIGN KEY (address_id)  REFERENCES addresses(id),
    INDEX       idx_bookings_status  (status),
    INDEX       idx_bookings_time    (scheduled_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- BOOKING ITEMS
-- The specific services requested in a booking.
-- ============================================================
CREATE TABLE IF NOT EXISTS booking_items (
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    booking_id          BIGINT UNSIGNED     NOT NULL,
    service_id          BIGINT UNSIGNED     NOT NULL,
    price               DECIMAL(10, 2)      NOT NULL COMMENT 'Snapshot of service price at time of booking',
    quantity            INT UNSIGNED        NOT NULL DEFAULT 1,
    
    created_at          DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    CONSTRAINT  fk_booking_items_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT  fk_booking_items_service FOREIGN KEY (service_id) REFERENCES services(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- PAYMENTS
-- Financial transactions associated with a booking.
-- ============================================================
CREATE TABLE IF NOT EXISTS payments (
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    booking_id          BIGINT UNSIGNED     NOT NULL,
    transaction_id      VARCHAR(255)                 COMMENT 'External gateway ID (e.g. Razorpay payment ID)',
    amount              DECIMAL(10, 2)      NOT NULL,
    status              VARCHAR(30)         NOT NULL DEFAULT 'PENDING' 
                            COMMENT 'PENDING | SUCCESS | FAILED | REFUNDED',
    payment_method      VARCHAR(50)                  COMMENT 'CARD | UPI | CASH | WALLET',
    
    created_at          DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at          DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_payments_txn (transaction_id),
    CONSTRAINT  fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    INDEX       idx_payments_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- REVIEWS
-- Feedback left after a booking is completed.
-- ============================================================
CREATE TABLE IF NOT EXISTS reviews (
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    booking_id          BIGINT UNSIGNED     NOT NULL,
    reviewer_id         BIGINT UNSIGNED     NOT NULL COMMENT 'The user writing the review (usually Customer)',
    reviewee_id         BIGINT UNSIGNED     NOT NULL COMMENT 'The user receiving the review (usually Provider)',
    rating              INT UNSIGNED        NOT NULL COMMENT '1 to 5 stars',
    comment             TEXT,
    
    created_at          DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3),

    PRIMARY KEY (id),
    UNIQUE KEY  uk_reviews_booking (booking_id),
    CONSTRAINT  fk_reviews_booking  FOREIGN KEY (booking_id)  REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT  fk_reviews_reviewer FOREIGN KEY (reviewer_id) REFERENCES users(id),
    CONSTRAINT  fk_reviews_reviewee FOREIGN KEY (reviewee_id) REFERENCES users(id),
    INDEX       idx_reviews_rating  (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
