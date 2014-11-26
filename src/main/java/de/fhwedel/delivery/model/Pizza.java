package de.fhwedel.delivery.model;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PIZZAS")
public class Pizza {
    private Long id;
    private List<Ingredient> ingredients;

    public Pizza() {
    }

    public Pizza(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public static Pizza empty() {
        return new Pizza(new ArrayList<Ingredient>());
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    @ManyToMany
    @JoinColumn(name="INGREDIENT_ID")
    @Cascade({CascadeType.SAVE_UPDATE})
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Pizza addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Pizza.class).addValue(this.id).addValue(this.ingredients).toString();
    }
}
