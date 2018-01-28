package de.pareus.hiptest.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Watchlist entity.
 */
public class WatchlistDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WatchlistDTO watchlistDTO = (WatchlistDTO) o;
        if(watchlistDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), watchlistDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WatchlistDTO{" +
            "id=" + getId() +
            "}";
    }
}
