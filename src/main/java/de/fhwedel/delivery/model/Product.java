package de.fhwedel.delivery.model;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCTS")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class Product {
    private Long id;

    public Product() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
