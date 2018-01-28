package de.pareus.hiptest.service.mapper;

import de.pareus.hiptest.domain.*;
import de.pareus.hiptest.service.dto.CustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {WatchlistMapper.class, AddressMapper.class})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {

    @Mapping(source = "watchlist.id", target = "watchlistId")
    @Mapping(source = "address.id", target = "addressId")
    CustomerDTO toDto(Customer customer);

    @Mapping(source = "watchlistId", target = "watchlist")
    @Mapping(source = "addressId", target = "address")
    Customer toEntity(CustomerDTO customerDTO);

    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
