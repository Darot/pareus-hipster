package de.pareus.hiptest.service.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Estate entity.
 */
public class EstateDTO implements Serializable {

    private Long id;

    private String description;

    private BigDecimal price;

    private Long addressId;

    private Long claimedById;

    private Long watchlistId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getClaimedById() {
        return claimedById;
    }

    public void setClaimedById(Long estateAgencyId) {
        this.claimedById = estateAgencyId;
    }

    public Long getWatchlistId() {
        return watchlistId;
    }

    public void setWatchlistId(Long watchlistId) {
        this.watchlistId = watchlistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EstateDTO estateDTO = (EstateDTO) o;
        if(estateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EstateDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
