package de.fhwedel.delivery.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "PURCHASE")
public class Purchase {
    private Long id;
    private Set<Product> products = Sets.newHashSet();
    private boolean billed = false;
    private boolean prepared = false;
    private boolean delivered = false;
    private Customer customer;

    private Purchase() {
    }

    public static Purchase empty() {
        return new Purchase();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Column(nullable = false)
    @OneToMany(mappedBy = "purchase")
    @Cascade({CascadeType.ALL})
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Purchase addProducts(Product... products) {
        for (Product product : products) {
            product.setPurchase(this);
        }

        Collections.addAll(this.products, products);
        return this;
    }

    @Column(nullable = false)
    public boolean isBilled() {
        return billed;
    }

    public void setBilled(boolean billed) {
        this.billed = billed;
    }

    @Column(nullable = false)
    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    @Column(nullable = false)
    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Purchase that = (Purchase) obj;

        return Objects.equal(this.id, that.id)
                && Objects.equal(this.products, that.products)
                && Objects.equal(this.billed, that.billed)
                && Objects.equal(this.delivered, that.delivered)
                && Objects.equal(this.prepared, that.prepared);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, products, billed, delivered, prepared);
    }

    public BigDecimal evaluateCost() {
        BigDecimal sum = new BigDecimal("0");
        for (Product product : products) {
            sum = sum.add(product.evaluateCost());
        }
        return sum;
    }
}
