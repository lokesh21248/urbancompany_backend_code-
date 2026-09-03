package com.urbanservices.catalog.dto;

import java.math.BigDecimal;
import java.util.List;

public class CategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String iconUrl;
    private Boolean isActive;
    private Integer sortOrder;
    private List<SubcategoryDTO> subcategories;

    public CategoryDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public List<SubcategoryDTO> getSubcategories() { return subcategories; }
    public void setSubcategories(List<SubcategoryDTO> subcategories) { this.subcategories = subcategories; }
}
