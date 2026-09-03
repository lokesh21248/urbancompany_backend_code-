package com.urbanservices.catalog.domain.repository;

import com.urbanservices.catalog.domain.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Optional<Subcategory> findBySlug(String slug);
    List<Subcategory> findByCategoryId(Long categoryId);
    List<Subcategory> findByIsActiveTrueOrderBySortOrderAsc();
    List<Subcategory> findAllByOrderBySortOrderAsc();
}
