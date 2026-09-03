package com.urbanservices.catalog.controller;

import com.urbanservices.catalog.dto.*;
import com.urbanservices.catalog.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // ── Categories ──────────────────────────────────────────────
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getCategories(
            @RequestParam(name = "activeOnly", defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(catalogService.getAllCategories(activeOnly));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getCategoryById(id));
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createCategory(request));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.ok(catalogService.updateCategory(id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        catalogService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ── Subcategories ──────────────────────────────────────────
    @GetMapping("/subcategories")
    public ResponseEntity<List<SubcategoryDTO>> getSubcategories(
            @RequestParam(name = "activeOnly", defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(catalogService.getAllSubcategories(activeOnly));
    }

    @GetMapping("/subcategories/{id}")
    public ResponseEntity<SubcategoryDTO> getSubcategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getSubcategoryById(id));
    }

    @PostMapping("/subcategories")
    public ResponseEntity<SubcategoryDTO> createSubcategory(@RequestBody SubcategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createSubcategory(request));
    }

    @PutMapping("/subcategories/{id}")
    public ResponseEntity<SubcategoryDTO> updateSubcategory(@PathVariable Long id, @RequestBody SubcategoryCreateRequest request) {
        return ResponseEntity.ok(catalogService.updateSubcategory(id, request));
    }

    @DeleteMapping("/subcategories/{id}")
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        catalogService.deleteSubcategory(id);
        return ResponseEntity.noContent().build();
    }

    // ── Services ────────────────────────────────────────────────
    @GetMapping("/services")
    public ResponseEntity<List<ServiceItemDTO>> getServices(
            @RequestParam(name = "subcategoryId", required = false) Long subcategoryId,
            @RequestParam(name = "activeOnly", defaultValue = "false") boolean activeOnly) {
        if (subcategoryId != null) {
            return ResponseEntity.ok(catalogService.getServicesBySubcategory(subcategoryId));
        }
        return ResponseEntity.ok(catalogService.getAllServices(activeOnly));
    }

    @GetMapping("/services/batch")
    public ResponseEntity<List<ServiceItemDTO>> getServicesByIds(@RequestParam(name = "ids") List<Long> ids) {
        return ResponseEntity.ok(catalogService.getServicesByIds(ids));
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<ServiceItemDTO> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getServiceById(id));
    }

    @PostMapping("/services")
    public ResponseEntity<ServiceItemDTO> createService(@RequestBody ServiceItemCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createService(request));
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<ServiceItemDTO> updateService(@PathVariable Long id, @RequestBody ServiceItemCreateRequest request) {
        return ResponseEntity.ok(catalogService.updateService(id, request));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        catalogService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
