package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.dto.request.MediaRequestDTO;
import com.PhamChien.ecommerce.dto.response.MediaResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;

import java.util.List;

public interface MediaService {
    MediaResponse saveMedia(MediaRequestDTO requestDTO);

    PageResponse<MediaResponse> getPagingMedias(int page, int size, String sortBy);

    List<MediaResponse> getAll();

    MediaResponse getById(Long mediaId);

    String delete(Long mediaId);

    List<MediaResponse> getAll(List<Long> mediaIds);
}
