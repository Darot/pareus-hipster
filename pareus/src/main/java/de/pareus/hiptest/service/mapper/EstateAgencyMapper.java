package de.pareus.hiptest.service.mapper;

import de.pareus.hiptest.domain.*;
import de.pareus.hiptest.service.dto.EstateAgencyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EstateAgency and its DTO EstateAgencyDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface EstateAgencyMapper extends EntityMapper<EstateAgencyDTO, EstateAgency> {

    @Mapping(source = "address.id", target = "addressId")
    EstateAgencyDTO toDto(EstateAgency estateAgency);

    @Mapping(source = "addressId", target = "address")
    EstateAgency toEntity(EstateAgencyDTO estateAgencyDTO);

    default EstateAgency fromId(Long id) {
        if (id == null) {
            return null;
        }
        EstateAgency estateAgency = new EstateAgency();
        estateAgency.setId(id);
        return estateAgency;
    }
}
