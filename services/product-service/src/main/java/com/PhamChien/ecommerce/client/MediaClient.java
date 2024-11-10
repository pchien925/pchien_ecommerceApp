package com.PhamChien.ecommerce.client;

import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.MediaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "media-service", url = "http://localhost:8086/api/v1/medias")
public interface MediaClient {
    @GetMapping(value = "/{mediaId}", produces = MediaType.ALL_VALUE)
    ApiResponse<MediaResponse> getMedia(@PathVariable("mediaId")long mediaId);

    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<MediaResponse>> getAllMedia(@RequestBody List<Long> mediaIds);
}
