package de.pareus.hiptest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Estate.
 */
@Entity
@Table(name = "estate")
@Document(indexName = "estate")
public class Estate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(mappedBy = "estate")
    @JsonIgnore
    private Set<Image> images = new HashSet<>();

    @ManyToOne
    private EstateAgency claimedBy;

    @ManyToOne
    private Watchlist watchlist;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Estate description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Estate price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Address getAddress() {
        return address;
    }

    public Estate address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Estate images(Set<Image> images) {
        this.images = images;
        return this;
    }

    public Estate addImage(Image image) {
        this.images.add(image);
        image.setEstate(this);
        return this;
    }

    public Estate removeImage(Image image) {
        this.images.remove(image);
        image.setEstate(null);
        return this;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public EstateAgency getClaimedBy() {
        return claimedBy;
    }

    public Estate claimedBy(EstateAgency estateAgency) {
        this.claimedBy = estateAgency;
        return this;
    }

    public void setClaimedBy(EstateAgency estateAgency) {
        this.claimedBy = estateAgency;
    }

    public Watchlist getWatchlist() {
        return watchlist;
    }

    public Estate watchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
        return this;
    }

    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Estate estate = (Estate) o;
        if (estate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Estate{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
