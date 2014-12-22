package de.fhwedel.delivery.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CUSTOMERS")
public class Customer {
    private Long id;
    private List<Order> orders = Lists.newArrayList();
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
    @OrderColumn(name = "ORDER_INDEX")
    @JoinColumn(name = "ORDER_ID")
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
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

    @OneToOne
    @JoinColumn(name = "ADDRESS_ID")
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
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
