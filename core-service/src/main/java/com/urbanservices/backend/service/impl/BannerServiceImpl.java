package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.domain.entity.Banner;
import com.urbanservices.backend.domain.repository.BannerRepository;
import com.urbanservices.backend.dto.BannerCreateRequest;
import com.urbanservices.backend.dto.BannerDTO;
import com.urbanservices.backend.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    public BannerServiceImpl(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    @Override
    public List<BannerDTO> getAllBanners(boolean activeOnly) {
        List<Banner> banners = activeOnly ? bannerRepository.findByIsActiveTrueOrderBySortOrderAsc() : bannerRepository.findAllByOrderBySortOrderAsc();
        List<BannerDTO> dtos = new ArrayList<>();
        for (Banner banner : banners) {
            BannerDTO dto = new BannerDTO();
            dto.setId(banner.getId());
            dto.setTitle(banner.getTitle());
            dto.setImageUrl(banner.getImageUrl());
            dto.setLinkUrl(banner.getLinkUrl());
            dto.setIsActive(banner.getIsActive());
            dto.setSortOrder(banner.getSortOrder());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public BannerDTO createBanner(BannerCreateRequest request) {
        Banner banner = new Banner();
        banner.setTitle(request.getTitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setLinkUrl(request.getLinkUrl());
        banner.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        banner.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        banner = bannerRepository.save(banner);
        
        BannerDTO dto = new BannerDTO();
        dto.setId(banner.getId());
        dto.setTitle(banner.getTitle());
        dto.setImageUrl(banner.getImageUrl());
        dto.setLinkUrl(banner.getLinkUrl());
        dto.setIsActive(banner.getIsActive());
        dto.setSortOrder(banner.getSortOrder());
        return dto;
    }

    @Override
    public BannerDTO updateBanner(Long id, BannerCreateRequest request) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Banner not found"));
        if (request.getTitle() != null) banner.setTitle(request.getTitle());
        if (request.getImageUrl() != null) banner.setImageUrl(request.getImageUrl());
        if (request.getLinkUrl() != null) banner.setLinkUrl(request.getLinkUrl());
        if (request.getIsActive() != null) banner.setIsActive(request.getIsActive());
        if (request.getSortOrder() != null) banner.setSortOrder(request.getSortOrder());
        banner = bannerRepository.save(banner);

        BannerDTO dto = new BannerDTO();
        dto.setId(banner.getId());
        dto.setTitle(banner.getTitle());
        dto.setImageUrl(banner.getImageUrl());
        dto.setLinkUrl(banner.getLinkUrl());
        dto.setIsActive(banner.getIsActive());
        dto.setSortOrder(banner.getSortOrder());
        return dto;
    }

    @Override
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}
