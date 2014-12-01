package de.fhwedel.delivery.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {
    private Long id;
    private List<Product> products = Lists.newArrayList();
    private boolean billed = false;

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
    @OrderColumn(name="PRODUCT_INDEX")
    @JoinColumn(name="PRODUCT_ID")
    @Cascade({CascadeType.ALL})
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Column(nullable = false)
    public boolean isBilled() {
        return billed;
    }

    public void setBilled(boolean billed) {
        this.billed = billed;
    }

    public Order addProduct(Product product) {
        products.add(product);
        return this;
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

    public BigDecimal getCost() {
        BigDecimal sum = new BigDecimal("0");
        for (Product product : products) {
            sum = sum.add(product.getCost());
        }
        return sum;
    }
}
