package com.PhamChien.ecommerce.Mapper;

import com.PhamChien.ecommerce.domain.Media;
import com.PhamChien.ecommerce.dto.response.CloudinaryResponse;
import com.PhamChien.ecommerce.dto.response.MediaResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    Media toMedia(CloudinaryResponse response);

    MediaResponse toMediaResponse(Media media);
}
