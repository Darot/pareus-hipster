package de.pareus.hiptest.service.mapper;

import de.pareus.hiptest.domain.*;
import de.pareus.hiptest.service.dto.ImageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Image and its DTO ImageDTO.
 */
@Mapper(componentModel = "spring", uses = {EstateMapper.class})
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {

    @Mapping(source = "estate.id", target = "estateId")
    ImageDTO toDto(Image image);

    @Mapping(source = "estateId", target = "estate")
    Image toEntity(ImageDTO imageDTO);

    default Image fromId(Long id) {
        if (id == null) {
            return null;
        }
        Image image = new Image();
        image.setId(id);
        return image;
    }
}
