-- ============================================================
-- queries.sql
-- Urban Services Platform — Queries for Every Table
-- Use in MySQL Workbench
-- ============================================================

USE urban_services_db;

-- ------------------------------------------------------------
-- 1. USERS TABLE
-- View all registered customers, providers, and admins
-- ------------------------------------------------------------
SELECT id, full_name, email, phone_number, role, status, created_at 
FROM users;


-- ------------------------------------------------------------
-- 2. ADDRESSES TABLE
-- View customer saved addresses with customer details
-- ------------------------------------------------------------
SELECT 
    a.id AS address_id,
    u.full_name AS customer_name,
    a.label,
    a.address_line_1,
    a.city,
    a.state,
    a.postal_code,
    a.is_default
FROM addresses a
JOIN users u ON a.user_id = u.id;


-- ------------------------------------------------------------
-- 3. PROVIDER PROFILES TABLE
-- View service provider ratings, verification status, and bio
-- ------------------------------------------------------------
SELECT 
    p.id AS profile_id,
    u.full_name AS provider_name,
    p.business_name,
    p.rating,
    p.total_reviews,
    p.verification_status,
    p.is_online
FROM provider_profiles p
JOIN users u ON p.user_id = u.id;


-- ------------------------------------------------------------
-- 4. CATEGORIES TABLE
-- View all top-level service categories
-- ------------------------------------------------------------
SELECT id, name, slug, description, is_active, sort_order 
FROM categories
ORDER BY sort_order ASC;


-- ------------------------------------------------------------
-- 5. SUBCATEGORIES TABLE
-- View subcategories grouped under parent categories
-- ------------------------------------------------------------
SELECT 
    sub.id AS subcategory_id,
    c.name AS parent_category,
    sub.name AS subcategory_name,
    sub.slug,
    sub.is_active
FROM subcategories sub
JOIN categories c ON sub.category_id = c.id
ORDER BY c.sort_order, sub.sort_order;


-- ------------------------------------------------------------
-- 6. SERVICES TABLE
-- View all bookable services, prices, and duration
-- ------------------------------------------------------------
SELECT 
    s.id AS service_id,
    c.name AS category,
    sub.name AS subcategory,
    s.name AS service_name,
    s.base_price,
    s.estimated_duration_minutes,
    s.is_active
FROM services s
JOIN subcategories sub ON s.subcategory_id = sub.id
JOIN categories c ON sub.category_id = c.id
ORDER BY s.base_price ASC;


-- ------------------------------------------------------------
-- 7. BOOKINGS TABLE
-- View all customer bookings, assigned provider, schedule & status
-- ------------------------------------------------------------
SELECT 
    b.id AS booking_id,
    cust.full_name AS customer_name,
    IFNULL(prov.full_name, 'Unassigned') AS provider_name,
    b.status,
    b.scheduled_time,
    b.total_amount,
    b.discount_amount,
    b.final_amount
FROM bookings b
JOIN users cust ON b.customer_id = cust.id
LEFT JOIN users prov ON b.provider_id = prov.id
ORDER BY b.scheduled_time DESC;


-- ------------------------------------------------------------
-- 8. BOOKING ITEMS TABLE
-- View specific services ordered inside each booking
-- ------------------------------------------------------------
SELECT 
    bi.id AS item_id,
    bi.booking_id,
    s.name AS service_name,
    bi.quantity,
    bi.price AS unit_price,
    (bi.quantity * bi.price) AS total_item_cost
FROM booking_items bi
JOIN services s ON bi.service_id = s.id;


-- ------------------------------------------------------------
-- 9. PAYMENTS TABLE
-- View payment status, transaction ID, and method
-- ------------------------------------------------------------
SELECT 
    p.id AS payment_id,
    p.booking_id,
    p.transaction_id,
    p.amount,
    p.payment_method,
    p.status AS payment_status,
    p.created_at
FROM payments p
ORDER BY p.created_at DESC;


-- ------------------------------------------------------------
-- 10. REVIEWS TABLE
-- View customer feedback and ratings for providers
-- ------------------------------------------------------------
SELECT 
    r.id AS review_id,
    r.booking_id,
    reviewer.full_name AS customer_name,
    reviewee.full_name AS provider_name,
    r.rating,
    r.comment,
    r.created_at
FROM reviews r
JOIN users reviewer ON r.reviewer_id = reviewer.id
JOIN users reviewee ON r.reviewee_id = reviewee.id;


-- ------------------------------------------------------------
-- 11. APP CONFIG TABLE
-- View platform settings (commission %, currency, limits)
-- ------------------------------------------------------------
SELECT id, config_key, config_value, value_type, description 
FROM app_config;


-- ------------------------------------------------------------
-- 12. MEDIA FILES TABLE
-- View uploaded assets, S3 keys, and URLs
-- ------------------------------------------------------------
SELECT id, public_id, entity_type, file_name, s3_bucket, content_type, status 
FROM media_files;


-- ------------------------------------------------------------
-- 13. AUDIT LOGS TABLE
-- View system security and business event trails
-- ------------------------------------------------------------
SELECT id, event_type, entity_type, entity_id, actor_type, ip_address, description, created_at 
FROM audit_logs
ORDER BY created_at DESC;
