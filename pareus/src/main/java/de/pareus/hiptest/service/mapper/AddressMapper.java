package de.pareus.hiptest.service.mapper;

import de.pareus.hiptest.domain.*;
import de.pareus.hiptest.service.dto.AddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Address and its DTO AddressDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {



    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
