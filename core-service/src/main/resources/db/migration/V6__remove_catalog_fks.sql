-- ============================================================
-- V6__remove_catalog_fks.sql
-- Urban Services Platform — Microservices Migration
-- Remove foreign key constraints to catalog tables from core
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- Drop FK from booking_items to services
ALTER TABLE booking_items DROP FOREIGN KEY fk_booking_items_service;

-- Drop FK from provider_services to services
ALTER TABLE provider_services DROP FOREIGN KEY fk_ps_service;

SET FOREIGN_KEY_CHECKS = 1;
