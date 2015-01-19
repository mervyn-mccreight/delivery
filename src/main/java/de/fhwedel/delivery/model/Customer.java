package de.fhwedel.delivery.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.ALL;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Entity
@Table(name = "CUSTOMER")
public class Customer {
    private Long id;
    private Set<Purchase> purchases = Sets.newHashSet();
    private String firstName;
    private String surName;
    private Address address;

    private Customer() {
    }

    public Customer(String firstName, String surName, Address address) {
        this.firstName = firstName;
        this.surName = surName;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    @OneToMany(mappedBy = "customer")
    @Cascade({ALL})
    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Customer addPurchase(Purchase purchase) {
        purchases.add(purchase);
        purchase.setCustomer(this);
        return this;
    }

    @Column(nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(nullable = false)
    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID", nullable = false)
    @Cascade({SAVE_UPDATE})
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, purchases, firstName, surName, address);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Customer that = (Customer) obj;

        return Objects.equal(this.id, that.id)
                && Objects.equal(this.purchases, that.purchases)
                && Objects.equal(this.firstName, that.firstName)
                && Objects.equal(this.surName, that.surName)
                && Objects.equal(this.address, that.address);
    }
}
