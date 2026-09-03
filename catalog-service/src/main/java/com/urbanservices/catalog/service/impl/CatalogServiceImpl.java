package com.urbanservices.catalog.service.impl;

import com.urbanservices.catalog.dto.*;
import com.urbanservices.catalog.domain.entity.*;
import com.urbanservices.catalog.domain.repository.*;
import com.urbanservices.catalog.service.CatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ServiceItemRepository serviceItemRepository;

    public CatalogServiceImpl(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, ServiceItemRepository serviceItemRepository) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    private String generateSlug(String input) {
        if (input == null || input.isBlank()) return "item-" + System.currentTimeMillis();
        return input.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    // ── Categories ──────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories(boolean activeOnly) {
        List<Category> categories = activeOnly 
                ? categoryRepository.findByIsActiveTrueOrderBySortOrderAsc() 
                : categoryRepository.findAllByOrderBySortOrderAsc();
        List<CategoryDTO> dtos = new ArrayList<>();
        for (Category c : categories) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setSlug(c.getSlug());
            dto.setDescription(c.getDescription());
            dto.setIconUrl(c.getIconUrl());
            dto.setIsActive(c.getActive());
            dto.setSortOrder(c.getSortOrder());

            List<SubcategoryDTO> subDtos = new ArrayList<>();
            if (c.getSubcategories() != null) {
                for (Subcategory s : c.getSubcategories()) {
                    if (activeOnly && !Boolean.TRUE.equals(s.getActive())) continue;
                    SubcategoryDTO sDto = new SubcategoryDTO();
                    sDto.setId(s.getId());
                    sDto.setCategoryId(c.getId());
                    sDto.setCategoryName(c.getName());
                    sDto.setName(s.getName());
                    sDto.setSlug(s.getSlug());
                    sDto.setDescription(s.getDescription());
                    sDto.setImageUrl(s.getImageUrl());
                    sDto.setIsActive(s.getActive());
                    sDto.setSortOrder(s.getSortOrder());

                    List<ServiceItemDTO> svcDtos = new ArrayList<>();
                    if (s.getServices() != null) {
                        for (ServiceItem item : s.getServices()) {
                            if (activeOnly && !Boolean.TRUE.equals(item.getActive())) continue;
                            ServiceItemDTO itemDto = new ServiceItemDTO();
                            itemDto.setId(item.getId());
                            itemDto.setName(item.getName());
                            itemDto.setSlug(item.getSlug());
                            itemDto.setDescription(item.getDescription());
                            itemDto.setBasePrice(item.getBasePrice());
                            itemDto.setIsActive(item.getActive());
                            itemDto.setEstimatedDurationMinutes(item.getEstimatedDurationMinutes());
                            itemDto.setSubcategoryId(s.getId());
                            itemDto.setSubcategoryName(s.getName());
                            itemDto.setImageUrl(item.getImageUrl());
                            svcDtos.add(itemDto);
                        }
                    }
                    sDto.setServices(svcDtos);
                    subDtos.add(sDto);
                }
            }
            dto.setSubcategories(subDtos);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category c = categoryRepository.findById(id).orElse(null);
        if (c == null) return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setSlug(c.getSlug());
        dto.setDescription(c.getDescription());
        dto.setIconUrl(c.getIconUrl());
        dto.setIsActive(c.getActive());
        dto.setSortOrder(c.getSortOrder());
        return dto;
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = generateSlug(request.getName());
        }
        category.setSlug(slug);
        category.setDescription(request.getDescription());
        category.setIconUrl(request.getIconUrl());
        category.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        category = categoryRepository.save(category);

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setIconUrl(category.getIconUrl());
        dto.setIsActive(category.getActive());
        dto.setSortOrder(category.getSortOrder());
        dto.setSubcategories(new ArrayList<>());
        return dto;
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        if (request.getName() != null) category.setName(request.getName());
        if (request.getSlug() != null && !request.getSlug().isBlank()) category.setSlug(request.getSlug());
        if (request.getDescription() != null) category.setDescription(request.getDescription());
        if (request.getIconUrl() != null) category.setIconUrl(request.getIconUrl());
        if (request.getIsActive() != null) category.setActive(request.getIsActive());
        if (request.getSortOrder() != null) category.setSortOrder(request.getSortOrder());
        category = categoryRepository.save(category);

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setIconUrl(category.getIconUrl());
        dto.setIsActive(category.getActive());
        dto.setSortOrder(category.getSortOrder());
        return dto;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // ── Subcategories ──────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoryDTO> getAllSubcategories(boolean activeOnly) {
        List<Subcategory> list = activeOnly 
                ? subcategoryRepository.findByIsActiveTrueOrderBySortOrderAsc() 
                : subcategoryRepository.findAllByOrderBySortOrderAsc();
        List<SubcategoryDTO> dtos = new ArrayList<>();
        for (Subcategory s : list) {
            SubcategoryDTO dto = new SubcategoryDTO();
            dto.setId(s.getId());
            if (s.getCategory() != null) {
                dto.setCategoryId(s.getCategory().getId());
                dto.setCategoryName(s.getCategory().getName());
            }
            dto.setName(s.getName());
            dto.setSlug(s.getSlug());
            dto.setDescription(s.getDescription());
            dto.setImageUrl(s.getImageUrl());
            dto.setIsActive(s.getActive());
            dto.setSortOrder(s.getSortOrder());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public SubcategoryDTO getSubcategoryById(Long id) {
        Subcategory s = subcategoryRepository.findById(id).orElse(null);
        if (s == null) return null;
        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setId(s.getId());
        if (s.getCategory() != null) {
            dto.setCategoryId(s.getCategory().getId());
            dto.setCategoryName(s.getCategory().getName());
        }
        dto.setName(s.getName());
        dto.setSlug(s.getSlug());
        dto.setDescription(s.getDescription());
        dto.setImageUrl(s.getImageUrl());
        dto.setIsActive(s.getActive());
        dto.setSortOrder(s.getSortOrder());
        return dto;
    }

    @Override
    @Transactional
    public SubcategoryDTO createSubcategory(SubcategoryCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
        Subcategory sub = new Subcategory();
        sub.setCategory(category);
        sub.setName(request.getName());
        sub.setSlug(generateSlug(request.getName()));
        sub.setDescription(request.getDescription());
        sub.setImageUrl(request.getImageUrl());
        sub.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        sub.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        sub = subcategoryRepository.save(sub);

        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setId(sub.getId());
        dto.setCategoryId(category.getId());
        dto.setCategoryName(category.getName());
        dto.setName(sub.getName());
        dto.setSlug(sub.getSlug());
        dto.setDescription(sub.getDescription());
        dto.setImageUrl(sub.getImageUrl());
        dto.setIsActive(sub.getActive());
        dto.setSortOrder(sub.getSortOrder());
        dto.setServices(new ArrayList<>());
        return dto;
    }

    @Override
    @Transactional
    public SubcategoryDTO updateSubcategory(Long id, SubcategoryCreateRequest request) {
        Subcategory sub = subcategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Subcategory not found"));
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            sub.setCategory(category);
        }
        if (request.getName() != null) sub.setName(request.getName());
        if (request.getDescription() != null) sub.setDescription(request.getDescription());
        if (request.getImageUrl() != null) sub.setImageUrl(request.getImageUrl());
        if (request.getIsActive() != null) sub.setActive(request.getIsActive());
        if (request.getSortOrder() != null) sub.setSortOrder(request.getSortOrder());
        sub = subcategoryRepository.save(sub);

        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setId(sub.getId());
        if (sub.getCategory() != null) {
            dto.setCategoryId(sub.getCategory().getId());
            dto.setCategoryName(sub.getCategory().getName());
        }
        dto.setName(sub.getName());
        dto.setSlug(sub.getSlug());
        dto.setDescription(sub.getDescription());
        dto.setImageUrl(sub.getImageUrl());
        dto.setIsActive(sub.getActive());
        dto.setSortOrder(sub.getSortOrder());
        return dto;
    }

    @Override
    @Transactional
    public void deleteSubcategory(Long id) {
        subcategoryRepository.deleteById(id);
    }

    // ── Services ────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<ServiceItemDTO> getAllServices(boolean activeOnly) {
        List<ServiceItem> list = activeOnly 
                ? serviceItemRepository.findByIsActiveTrueOrderBySortOrderAsc() 
                : serviceItemRepository.findAllByOrderBySortOrderAsc();
        List<ServiceItemDTO> dtos = new ArrayList<>();
        for (ServiceItem item : list) {
            ServiceItemDTO dto = new ServiceItemDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setSlug(item.getSlug());
            dto.setDescription(item.getDescription());
            dto.setBasePrice(item.getBasePrice());
            dto.setIsActive(item.getActive());
            dto.setEstimatedDurationMinutes(item.getEstimatedDurationMinutes());
            if (item.getSubcategory() != null) {
                dto.setSubcategoryId(item.getSubcategory().getId());
                dto.setSubcategoryName(item.getSubcategory().getName());
                if (item.getSubcategory().getCategory() != null) {
                    dto.setCategoryName(item.getSubcategory().getCategory().getName());
                }
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceItemDTO> getServicesBySubcategory(Long subcategoryId) {
        List<ServiceItem> list = serviceItemRepository.findBySubcategoryId(subcategoryId);
        List<ServiceItemDTO> dtos = new ArrayList<>();
        for (ServiceItem item : list) {
            ServiceItemDTO dto = new ServiceItemDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setSlug(item.getSlug());
            dto.setBasePrice(item.getBasePrice());
            dto.setIsActive(item.getActive());
            dto.setEstimatedDurationMinutes(item.getEstimatedDurationMinutes());
            if (item.getSubcategory() != null) {
                dto.setSubcategoryId(item.getSubcategory().getId());
                dto.setSubcategoryName(item.getSubcategory().getName());
            }
            dto.setImageUrl(item.getImageUrl());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceItemDTO getServiceById(Long id) {
        ServiceItem item = serviceItemRepository.findById(id).orElse(null);
        if (item == null) return null;
        ServiceItemDTO dto = new ServiceItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setSlug(item.getSlug());
        dto.setDescription(item.getDescription());
        dto.setDetailedDescription(item.getDetailedDescription());
        dto.setBasePrice(item.getBasePrice());
        dto.setIsActive(item.getActive());
        dto.setEstimatedDurationMinutes(item.getEstimatedDurationMinutes());
        if (item.getSubcategory() != null) {
            dto.setSubcategoryId(item.getSubcategory().getId());
            dto.setSubcategoryName(item.getSubcategory().getName());
        }
        dto.setImageUrl(item.getImageUrl());
        return dto;
    }

    @Override
    @Transactional
    public ServiceItemDTO createService(ServiceItemCreateRequest request) {
        Subcategory subcategory = subcategoryRepository.findById(request.getSubcategoryId())
                .orElseThrow(() -> new RuntimeException("Subcategory not found with id: " + request.getSubcategoryId()));
        ServiceItem item = new ServiceItem();
        item.setSubcategory(subcategory);
        item.setName(request.getName());
        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = generateSlug(request.getName());
        }
        item.setSlug(slug);
        item.setDescription(request.getDescription());
        item.setDetailedDescription(request.getDetailedDescription());
        item.setBasePrice(request.getBasePrice());
        item.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes() != null ? request.getEstimatedDurationMinutes() : 60);
        item.setImageUrl(request.getImageUrl());
        item.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        item.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        item = serviceItemRepository.save(item);

        ServiceItemDTO dto = new ServiceItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setSlug(item.getSlug());
        dto.setDescription(item.getDescription());
        dto.setBasePrice(item.getBasePrice());
        dto.setIsActive(item.getActive());
        dto.setEstimatedDurationMinutes(item.getEstimatedDurationMinutes());
        dto.setSubcategoryId(subcategory.getId());
        dto.setSubcategoryName(subcategory.getName());
        dto.setImageUrl(item.getImageUrl());
        return dto;
    }

    @Override
    @Transactional
    public ServiceItemDTO updateService(Long id, ServiceItemCreateRequest request) {
        ServiceItem item = serviceItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        if (request.getSubcategoryId() != null) {
            Subcategory subcategory = subcategoryRepository.findById(request.getSubcategoryId())
                    .orElseThrow(() -> new RuntimeException("Subcategory not found"));
            item.setSubcategory(subcategory);
        }
        if (request.getName() != null) item.setName(request.getName());
        if (request.getSlug() != null && !request.getSlug().isBlank()) item.setSlug(request.getSlug());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getDetailedDescription() != null) item.setDetailedDescription(request.getDetailedDescription());
        if (request.getBasePrice() != null) item.setBasePrice(request.getBasePrice());
        if (request.getEstimatedDurationMinutes() != null) item.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes());
        if (request.getImageUrl() != null) item.setImageUrl(request.getImageUrl());
        if (request.getIsActive() != null) item.setActive(request.getIsActive());
        if (request.getSortOrder() != null) item.setSortOrder(request.getSortOrder());
        item = serviceItemRepository.save(item);

        ServiceItemDTO dto = new ServiceItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setSlug(item.getSlug());
        dto.setDescription(item.getDescription());
        dto.setBasePrice(item.getBasePrice());
        dto.setIsActive(item.getActive());
        dto.setEstimatedDurationMinutes(item.getEstimatedDurationMinutes());
        if (item.getSubcategory() != null) {
            dto.setSubcategoryId(item.getSubcategory().getId());
            dto.setSubcategoryName(item.getSubcategory().getName());
        }
        dto.setImageUrl(item.getImageUrl());
        return dto;
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        serviceItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceItemDTO> getServicesByIds(List<Long> ids) {
        List<ServiceItemDTO> dtos = new ArrayList<>();
        List<ServiceItem> items = serviceItemRepository.findAllById(ids);
        for (ServiceItem item : items) {
            ServiceItemDTO dto = new ServiceItemDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setSlug(item.getSlug());
            dto.setBasePrice(item.getBasePrice());
            dto.setIsActive(item.getActive());
            dto.setEstimatedDurationMinutes(item.getEstimatedDurationMinutes());
            if (item.getSubcategory() != null) {
                dto.setSubcategoryId(item.getSubcategory().getId());
                dto.setSubcategoryName(item.getSubcategory().getName());
            }
            dtos.add(dto);
        }
        return dtos;
    }
}
