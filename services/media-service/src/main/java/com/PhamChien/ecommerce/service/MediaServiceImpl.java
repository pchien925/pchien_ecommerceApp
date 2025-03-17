package com.PhamChien.ecommerce.service;

import com.PhamChien.ecommerce.Mapper.MediaMapper;
import com.PhamChien.ecommerce.Repository.MediaRepository;
import com.PhamChien.ecommerce.domain.Media;
import com.PhamChien.ecommerce.dto.request.MediaRequestDTO;
import com.PhamChien.ecommerce.dto.response.CloudinaryResponse;
import com.PhamChien.ecommerce.dto.response.MediaResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.exception.InvalidDataException;
import com.PhamChien.ecommerce.exception.ResourceNotFoundException;
import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final Cloudinary cloudinary;
    private final MediaMapper mediaMapper;
    private final MediaRepository mediaRepository;

    @Override
    public MediaResponse saveMedia(MediaRequestDTO requestDTO) {
        if (mediaRepository.existsByMediaKey(requestDTO.getMediaKey()))
            throw new InvalidDataException("media existed");
        CloudinaryResponse response = this.uploadFile(requestDTO);
        Media media = mediaMapper.toMedia(response);
        return mediaMapper.toMediaResponse(mediaRepository.save(media));
    }

    @Override
    public PageResponse<MediaResponse> getPagingMedias(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortBy));

        Page<Media> mediaPage = mediaRepository.findAll(pageable);

        return PageResponse.<MediaResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(mediaPage.getTotalPages())
                .totalElements(mediaPage.getTotalElements())
                .content(mediaPage.getContent().stream().map(mediaMapper::toMediaResponse).toList())
                .build();
    }

    @Override
    public List<MediaResponse> getAll() {
        return mediaRepository.findAll().stream().map(mediaMapper::toMediaResponse).toList();
    }

    @Override
    public MediaResponse getById(Long mediaId) {
        return mediaMapper.toMediaResponse(mediaRepository.findById(mediaId).orElseThrow(() -> new ResourceNotFoundException("media not found")));
    }

    @Override
    public String delete(Long mediaId) {
        Media media = mediaRepository.findById(mediaId).orElseThrow(() -> new ResourceNotFoundException("media not found"));
        if(this.remove(media.getMediaKey()))
            mediaRepository.delete(media);
        return "Deleted";
    }

    @Override
    public MediaResponse upload(MultipartFile multipartFile) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            Media media = Media.builder()
                    .url(result.get("url").toString())
                    .mediaKey(result.get("public_id").toString())
                    .type(result.get("resource_type").toString())
                    .build();
            return mediaMapper.toMediaResponse(mediaRepository.save(media));
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<MediaResponse> getAll(List<Long> mediaIds){
        return mediaRepository.findAllById(mediaIds).stream().map(mediaMapper::toMediaResponse).toList();
    }

    private CloudinaryResponse uploadFile(MediaRequestDTO requestDTO) {
        String public_id = null;
        try {
            Map<?, ?> result = cloudinary.uploader().upload(requestDTO.getFile().getBytes(),
                    Map.of("public_id", requestDTO.getMediaKey())
            );
            public_id = (String) result.get("public_id");

            return CloudinaryResponse.builder()
                    .mediaKey(public_id)
                    .caption(requestDTO.getCaption())
                    .url(result.get("url").toString())
                    .type(result.get("resource_type").toString())
                    .build();

        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Boolean remove(String publicId) {
        try {
            ApiResponse response = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            if(response != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
