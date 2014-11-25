package de.fhwedel.delivery.model;


import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity()
@Table(name = "INGREDIENTS")
public class Ingredient {

    public static final Ingredient CHEESE = new Ingredient(1l, "Cheese", new BigDecimal("1.00"));
    public static final Ingredient TOMATO_SAUCE = new Ingredient(2l, "Tomato Sauce", new BigDecimal("2.00"));
    public static final Ingredient SALAMI = new Ingredient(3l, "Salami", new BigDecimal("3.00"));

    private Long id;

    private String name;
    private BigDecimal cost;

    public Ingredient() {
    }

    private Ingredient(Long id, String name, BigDecimal cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Ingredient.class)
                .addValue(this.id)
                .addValue(this.name)
                .addValue(this.cost)
                .toString();
    }
}
