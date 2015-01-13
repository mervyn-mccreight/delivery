package de.fhwedel.delivery.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CUSTOMERS")
public class Customer {
    private Long id;
    private Set<Order> orders = Sets.newHashSet();
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
    @SequenceGenerator(name = "CUSTOMER_ID_GENERATOR", sequenceName = "CUSTOMER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_ID_GENERATOR")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    @OneToMany
    @JoinColumn(name = "CUSTOMER_ID")
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Customer addOrder(Order order) {
        orders.add(order);
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

    // TODO: value-type?
    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID")
    @Cascade({CascadeType.SAVE_UPDATE})
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, orders, firstName, surName, address);
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
                && Objects.equal(this.orders, that.orders)
                && Objects.equal(this.firstName, that.firstName)
                && Objects.equal(this.surName, that.surName)
                && Objects.equal(this.address, that.address);
    }
}
