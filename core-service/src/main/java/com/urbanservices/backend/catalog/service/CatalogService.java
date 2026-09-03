package com.urbanservices.catalog.service;

import com.urbanservices.catalog.dto.*;
import java.util.List;

public interface CatalogService {
    List<CategoryDTO> getAllCategories(boolean activeOnly);
    CategoryDTO getCategoryById(Long id);
    CategoryDTO createCategory(CategoryCreateRequest request);
    CategoryDTO updateCategory(Long id, CategoryCreateRequest request);
    void deleteCategory(Long id);

    List<ServiceItemDTO> getAllServices(boolean activeOnly);
    List<ServiceItemDTO> getServicesByIds(List<Long> ids);
    List<ServiceItemDTO> getServicesBySubcategory(Long subcategoryId);
    ServiceItemDTO getServiceById(Long id);
    ServiceItemDTO createService(ServiceItemCreateRequest request);
    ServiceItemDTO updateService(Long id, ServiceItemCreateRequest request);
    void deleteService(Long id);

    List<SubcategoryDTO> getAllSubcategories(boolean activeOnly);
    SubcategoryDTO getSubcategoryById(Long id);
    SubcategoryDTO createSubcategory(SubcategoryCreateRequest request);
    SubcategoryDTO updateSubcategory(Long id, SubcategoryCreateRequest request);
    void deleteSubcategory(Long id);
}
