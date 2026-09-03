package com.urbanservices.catalog.domain.repository;

import com.urbanservices.catalog.domain.entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    Optional<ServiceItem> findBySlug(String slug);
    List<ServiceItem> findBySubcategoryId(Long subcategoryId);
    List<ServiceItem> findByIsActiveTrue();
    List<ServiceItem> findByIsActiveTrueOrderBySortOrderAsc();
    List<ServiceItem> findAllByOrderBySortOrderAsc();
    List<ServiceItem> findByIdIn(List<Long> ids);
}
