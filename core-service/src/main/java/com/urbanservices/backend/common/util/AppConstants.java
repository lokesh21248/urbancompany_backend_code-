package com.urbanservices.backend.common.util;

/**
 * Application-wide constants for the Urban Services platform.
 *
 * <p>These are compile-time constants shared across modules.
 * Runtime configuration lives in application.yml / app_config table.
 */
public final class AppConstants {

    private AppConstants() { /* utility class — no instantiation */ }

    // ── API ──────────────────────────────────────────────────────────────
    public static final String API_VERSION            = "v1";
    public static final String API_PREFIX             = "/api/" + API_VERSION;

    // ── Pagination defaults ──────────────────────────────────────────────
    public static final int    DEFAULT_PAGE           = 0;
    public static final int    DEFAULT_PAGE_SIZE      = 20;
    public static final int    MAX_PAGE_SIZE          = 100;

    // ── User roles ───────────────────────────────────────────────────────
    public static final String ROLE_CUSTOMER          = "CUSTOMER";
    public static final String ROLE_PROVIDER          = "PROVIDER";
    public static final String ROLE_ADMIN             = "ADMIN";
    public static final String ROLE_SUPER_ADMIN       = "SUPER_ADMIN";

    // ── User statuses ────────────────────────────────────────────────────
    public static final String STATUS_ACTIVE          = "ACTIVE";
    public static final String STATUS_INACTIVE        = "INACTIVE";
    public static final String STATUS_BLOCKED         = "BLOCKED";
    public static final String STATUS_SUSPENDED       = "SUSPENDED";
    public static final String STATUS_DELETED         = "DELETED";

    // ── Provider statuses ────────────────────────────────────────────────
    public static final String PROVIDER_PENDING       = "PENDING";
    public static final String PROVIDER_UNDER_REVIEW  = "UNDER_REVIEW";
    public static final String PROVIDER_APPROVED      = "APPROVED";
    public static final String PROVIDER_REJECTED      = "REJECTED";
    public static final String PROVIDER_SUSPENDED     = "SUSPENDED";

    // ── Media entity types ────────────────────────────────────────────────
    public static final String MEDIA_CATEGORY         = "CATEGORY";
    public static final String MEDIA_SUBCATEGORY      = "SUBCATEGORY";
    public static final String MEDIA_SERVICE          = "SERVICE";
    public static final String MEDIA_PROVIDER         = "PROVIDER";
    public static final String MEDIA_CUSTOMER         = "CUSTOMER";
    public static final String MEDIA_BANNER           = "BANNER";
    public static final String MEDIA_DOCUMENT         = "DOCUMENT";

    // ── Audit event types ─────────────────────────────────────────────────
    public static final String AUDIT_USER_REGISTERED       = "USER_REGISTERED";
    public static final String AUDIT_LOGIN_SUCCESS         = "LOGIN_SUCCESS";
    public static final String AUDIT_LOGIN_FAILED          = "LOGIN_FAILED";
    public static final String AUDIT_PASSWORD_CHANGED      = "PASSWORD_CHANGED";
    public static final String AUDIT_PROFILE_UPDATED       = "PROFILE_UPDATED";
    public static final String AUDIT_ADDRESS_CREATED       = "ADDRESS_CREATED";
    public static final String AUDIT_ADDRESS_UPDATED       = "ADDRESS_UPDATED";
    public static final String AUDIT_ADDRESS_DELETED       = "ADDRESS_DELETED";
    public static final String AUDIT_BOOKING_CREATED       = "BOOKING_CREATED";
    public static final String AUDIT_BOOKING_CANCELLED     = "BOOKING_CANCELLED";
    public static final String AUDIT_PROVIDER_APPROVED     = "PROVIDER_APPROVED";
    public static final String AUDIT_PROVIDER_REJECTED     = "PROVIDER_REJECTED";
    public static final String AUDIT_PROVIDER_SUSPENDED    = "PROVIDER_SUSPENDED";

    // ── Address labels ────────────────────────────────────────────────────
    public static final String ADDRESS_HOME           = "HOME";
    public static final String ADDRESS_WORK           = "WORK";
    public static final String ADDRESS_OTHER          = "OTHER";

    // ── Booking statuses ──────────────────────────────────────────────────
    public static final String BOOKING_PENDING        = "PENDING";
    public static final String BOOKING_CONFIRMED      = "CONFIRMED";
    public static final String BOOKING_ASSIGNED       = "ASSIGNED";
    public static final String BOOKING_ACCEPTED       = "ACCEPTED";
    public static final String BOOKING_ON_THE_WAY     = "ON_THE_WAY";
    public static final String BOOKING_ARRIVED        = "ARRIVED";
    public static final String BOOKING_IN_PROGRESS    = "IN_PROGRESS";
    public static final String BOOKING_COMPLETED      = "COMPLETED";
    public static final String BOOKING_CANCELLED      = "CANCELLED";

    // ── Payment statuses ──────────────────────────────────────────────────
    public static final String PAYMENT_PENDING        = "PENDING";
    public static final String PAYMENT_SUCCESS        = "SUCCESS";
    public static final String PAYMENT_FAILED         = "FAILED";
    public static final String PAYMENT_REFUNDED       = "REFUNDED";
    public static final String PAYMENT_PARTIAL_REFUND = "PARTIALLY_REFUNDED";

    // ── S3 folder structure ───────────────────────────────────────────────
    public static final String S3_FOLDER_CATEGORIES   = "categories/";
    public static final String S3_FOLDER_SERVICES     = "services/";
    public static final String S3_FOLDER_PROVIDERS    = "providers/";
    public static final String S3_FOLDER_CUSTOMERS    = "customers/";
    public static final String S3_FOLDER_BANNERS      = "banners/";
    public static final String S3_FOLDER_DOCUMENTS    = "documents/";

    // ── Validation patterns ───────────────────────────────────────────────
    /** Indian mobile number: 10 digits starting with 6-9 */
    public static final String REGEX_MOBILE_IN        = "^[6-9]\\d{9}$";
    /** Generic international mobile: E.164 without '+' prefix */
    public static final String REGEX_MOBILE_INTL      = "^\\+?[1-9]\\d{7,14}$";
    /** Minimum 8 chars, at least 1 uppercase, 1 lowercase, 1 digit */
    public static final String REGEX_PASSWORD         = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    /** Postal/PIN code — 6 digits */
    public static final String REGEX_PINCODE_IN       = "^[1-9][0-9]{5}$";
}
