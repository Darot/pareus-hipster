package de.pareus.hiptest.service.mapper;

import de.pareus.hiptest.domain.*;
import de.pareus.hiptest.service.dto.EstateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Estate and its DTO EstateDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, EstateAgencyMapper.class, WatchlistMapper.class})
public interface EstateMapper extends EntityMapper<EstateDTO, Estate> {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "claimedBy.id", target = "claimedById")
    @Mapping(source = "watchlist.id", target = "watchlistId")
    EstateDTO toDto(Estate estate);

    @Mapping(source = "addressId", target = "address")
    @Mapping(target = "images", ignore = true)
    @Mapping(source = "claimedById", target = "claimedBy")
    @Mapping(source = "watchlistId", target = "watchlist")
    Estate toEntity(EstateDTO estateDTO);

    default Estate fromId(Long id) {
        if (id == null) {
            return null;
        }
        Estate estate = new Estate();
        estate.setId(id);
        return estate;
    }
}
