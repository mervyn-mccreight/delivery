package de.fhwedel.delivery.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class Product {
    private Long id;
    private Purchase purchase;

    public Product() {
    }

    @ManyToOne
    @JoinColumn(name = "PURCHASE_ID", nullable = false)
    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract BigDecimal evaluateCost();
}
