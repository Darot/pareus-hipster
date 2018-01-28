package de.pareus.hiptest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Watchlist.
 */
@Entity
@Table(name = "watchlist")
@Document(indexName = "watchlist")
public class Watchlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "watchlist")
    @JsonIgnore
    private Set<Estate> estates = new HashSet<>();

    @OneToOne(mappedBy = "watchlist")
    @JsonIgnore
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Estate> getEstates() {
        return estates;
    }

    public Watchlist estates(Set<Estate> estates) {
        this.estates = estates;
        return this;
    }

    public Watchlist addEstate(Estate estate) {
        this.estates.add(estate);
        estate.setWatchlist(this);
        return this;
    }

    public Watchlist removeEstate(Estate estate) {
        this.estates.remove(estate);
        estate.setWatchlist(null);
        return this;
    }

    public void setEstates(Set<Estate> estates) {
        this.estates = estates;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Watchlist customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        Watchlist watchlist = (Watchlist) o;
        if (watchlist.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), watchlist.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Watchlist{" +
            "id=" + getId() +
            "}";
    }
}
