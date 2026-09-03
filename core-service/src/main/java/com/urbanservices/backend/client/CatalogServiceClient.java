package com.urbanservices.backend.client;

import com.urbanservices.backend.dto.ServiceItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "catalog-service")
public interface CatalogServiceClient {

    @GetMapping("/api/v1/services/{id}")
    ServiceItemDTO getServiceById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/services")
    List<ServiceItemDTO> getAllServices();
    
    // In a real app we'd have a batch get by IDs, but for now we'll assume there's one or we get all
    // Let's add a batch endpoint signature, and we can add it to catalog-service later
    @GetMapping("/api/v1/services/batch")
    List<ServiceItemDTO> getServicesByIds(@RequestParam("ids") List<Long> ids);
}
