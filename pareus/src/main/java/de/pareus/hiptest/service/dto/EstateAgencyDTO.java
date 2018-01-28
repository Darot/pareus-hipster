package de.pareus.hiptest.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the EstateAgency entity.
 */
public class EstateAgencyDTO implements Serializable {

    private Long id;

    private String name;

    private Long addressId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EstateAgencyDTO estateAgencyDTO = (EstateAgencyDTO) o;
        if(estateAgencyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estateAgencyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EstateAgencyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
