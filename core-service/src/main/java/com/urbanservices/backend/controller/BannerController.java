package com.urbanservices.backend.controller;

import com.urbanservices.backend.dto.BannerCreateRequest;
import com.urbanservices.backend.dto.BannerDTO;
import com.urbanservices.backend.service.BannerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@CrossOrigin(origins = "*")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping
    public ResponseEntity<List<BannerDTO>> getAllBanners(
            @RequestParam(name = "activeOnly", defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(bannerService.getAllBanners(activeOnly));
    }

    @PostMapping
    public ResponseEntity<BannerDTO> createBanner(@RequestBody BannerCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bannerService.createBanner(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerDTO> updateBanner(@PathVariable Long id, @RequestBody BannerCreateRequest request) {
        return ResponseEntity.ok(bannerService.updateBanner(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }
}
