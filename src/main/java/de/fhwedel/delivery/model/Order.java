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
@Table(name = "ORDERS")
public class Order {
    private Long id;
    private Set<Product> products = Sets.newHashSet();
    private boolean billed = false;
    private boolean prepared = false;
    private boolean delivered = false;

    private Order() {
    }

    public static Order empty() {
        return new Order();
    }

    @Id
    @SequenceGenerator(name = "ORDER_ID_GENERATOR", sequenceName = "ORDER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_ID_GENERATOR")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    @OneToMany
    @JoinColumn(name="ORDER_ID")
    @Cascade({CascadeType.ALL})
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Order addProducts(Product... products) {
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

        Order that = (Order) obj;

        return Objects.equal(this.id, that.id)
                && Objects.equal(this.products, that.products)
                && Objects.equal(this.billed, that.billed);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, products, billed);
    }

    public BigDecimal evaluateCost() {
        BigDecimal sum = new BigDecimal("0");
        for (Product product : products) {
            sum = sum.add(product.evaluateCost());
        }
        return sum;
    }
}
