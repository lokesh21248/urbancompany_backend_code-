package com.urbanservices.backend.service;

import com.urbanservices.backend.dto.BannerDTO;
import com.urbanservices.backend.dto.BannerCreateRequest;
import java.util.List;

public interface BannerService {
    List<BannerDTO> getAllBanners(boolean activeOnly);
    BannerDTO createBanner(BannerCreateRequest request);
    BannerDTO updateBanner(Long id, BannerCreateRequest request);
    void deleteBanner(Long id);
}
