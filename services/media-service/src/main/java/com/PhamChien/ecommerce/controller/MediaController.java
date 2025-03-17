package com.PhamChien.ecommerce.controller;

import com.PhamChien.ecommerce.dto.request.MediaRequestDTO;
import com.PhamChien.ecommerce.dto.response.ApiResponse;
import com.PhamChien.ecommerce.dto.response.MediaResponse;
import com.PhamChien.ecommerce.dto.response.PageResponse;
import com.PhamChien.ecommerce.service.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/medias")
public class MediaController {
    private final MediaService mediaService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ApiResponse<MediaResponse> save(@ModelAttribute @Valid MediaRequestDTO requestDTO){
        return ApiResponse.<MediaResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(mediaService.saveMedia(requestDTO))   
                .build();
    }

    @PostMapping("/upload")
    ApiResponse<MediaResponse> upload(@RequestParam("file") MultipartFile multipartFile){
        return ApiResponse.<MediaResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(mediaService.upload(multipartFile))
                .build();
    }

    @GetMapping("/paging")
    ApiResponse<PageResponse<MediaResponse>> getPagingMedias(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy

    ){
        return ApiResponse.<PageResponse<MediaResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(mediaService.getPagingMedias(page, size, sortBy))
                .build();
    }

    @GetMapping
    ApiResponse<List<MediaResponse>> getAllMedias(){
        return ApiResponse.<List<MediaResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(mediaService.getAll())
                .build();
    }

    @GetMapping("/{mediaId}")
    ApiResponse<MediaResponse> getMediaById(@PathVariable("mediaId") Long mediaId){
        return ApiResponse.<MediaResponse>builder()
                .status(HttpStatus.OK.value())
                .data(mediaService.getById(mediaId))
                .build();
    }

    @DeleteMapping("/{mediaId}")
    ApiResponse<String> delete(@PathVariable("mediaId") Long mediaId){
        return ApiResponse.<String>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .data(mediaService.delete(mediaId))
                .build();
    }

    @PostMapping("/list")
    ApiResponse<List<MediaResponse>> getAllMedia(@RequestBody List<Long> mediaIds){
        return ApiResponse.<List<MediaResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(mediaService.getAll(mediaIds))
                .build();
    }
}
