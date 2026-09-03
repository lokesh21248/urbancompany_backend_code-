package com.urbanservices.backend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Paginated response envelope for list endpoints.
 *
 * <p>Wraps Spring Data's {@link Page} into a stable JSON contract
 * that Flutter can depend on. Decouples the API contract from
 * Spring Data's internal Page structure.
 *
 * <p>Example response:
 * <pre>
 * {
 *   "success": true,
 *   "message": "Success",
 *   "data": {
 *     "content": [...],
 *     "page": 0,
 *     "size": 20,
 *     "total_elements": 150,
 *     "total_pages": 8,
 *     "first": true,
 *     "last": false
 *   }
 * }
 * </pre>
 *
 * @param <T> The DTO type of each element in the page
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    /**
     * Convenience factory: build from a Spring Data {@link Page}.
     */
    public static <T> ApiResponse<PagedResponse<T>> of(Page<T> page) {
        PagedResponse<T> paged = PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
        return ApiResponse.success(paged);
    }

    /**
     * Convenience factory: build from a Spring Data {@link Page} with a custom message.
     */
    public static <T> ApiResponse<PagedResponse<T>> of(String message, Page<T> page) {
        PagedResponse<T> paged = PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
        return ApiResponse.success(message, paged);
    }
}
