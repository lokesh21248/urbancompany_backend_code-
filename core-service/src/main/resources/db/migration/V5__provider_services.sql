-- ============================================================
-- V5__provider_services.sql
-- Urban Services Platform — Provider Service Mapping
-- Database: MySQL 8.x
-- ============================================================

USE urban_services_db;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS provider_services (
    provider_id BIGINT UNSIGNED NOT NULL,
    service_id  BIGINT UNSIGNED NOT NULL,
    created_at  DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    
    PRIMARY KEY (provider_id, service_id),
    CONSTRAINT fk_ps_provider FOREIGN KEY (provider_id) REFERENCES provider_profiles(id) ON DELETE CASCADE,
    CONSTRAINT fk_ps_service FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Map existing demo providers to their matching services:
-- Rajesh (provider_id=1, Electrician) -> Services 3 (Switchboard), 4 (Ceiling Fan)
-- Anita (provider_id=2, Cleaning) -> Services 1 (Bathroom), 2 (Full Home 2BHK)
-- Vikram (provider_id=3, Plumber) -> Services 5 (Tap & Pipe), 6 (Drain blockage)
-- Priya (provider_id=4, Salon) -> Services 7 (Diamond Facial)
INSERT IGNORE INTO provider_services (provider_id, service_id)
VALUES
    (1, 3),
    (1, 4),
    (2, 1),
    (2, 2),
    (3, 5),
    (3, 6),
    (4, 7);

SET FOREIGN_KEY_CHECKS = 1;
