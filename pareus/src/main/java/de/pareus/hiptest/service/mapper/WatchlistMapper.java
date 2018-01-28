package de.pareus.hiptest.service.mapper;

import de.pareus.hiptest.domain.*;
import de.pareus.hiptest.service.dto.WatchlistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Watchlist and its DTO WatchlistDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WatchlistMapper extends EntityMapper<WatchlistDTO, Watchlist> {


    @Mapping(target = "estates", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Watchlist toEntity(WatchlistDTO watchlistDTO);

    default Watchlist fromId(Long id) {
        if (id == null) {
            return null;
        }
        Watchlist watchlist = new Watchlist();
        watchlist.setId(id);
        return watchlist;
    }
}
