package de.fhwedel.delivery.model;

import com.google.common.base.Objects;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESSES")
public class Address {
    private Long id;

    private String street;
    private String zip;
    private String city;
    private String country;

    public Address() {
    }

    public Address(String street, String zip, String city, String country) {
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    @Id
    @SequenceGenerator(name = "ADDRESS_ID_GENERATOR", sequenceName = "ADDRESS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_ID_GENERATOR")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Column(nullable = false)
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Column(nullable = false)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(nullable = false)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, street, zip, city, country);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Address that = (Address) obj;

        return Objects.equal(this.id, that.id)
                && Objects.equal(this.street, that.street)
                && Objects.equal(this.zip, that.zip)
                && Objects.equal(this.city, that.city)
                && Objects.equal(this.country, that.country);
    }
}
