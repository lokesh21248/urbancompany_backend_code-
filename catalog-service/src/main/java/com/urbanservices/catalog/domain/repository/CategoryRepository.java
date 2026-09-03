package com.urbanservices.catalog.domain.repository;

import com.urbanservices.catalog.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    List<Category> findAllByOrderBySortOrderAsc();
    List<Category> findByIsActiveTrueOrderBySortOrderAsc();
}
