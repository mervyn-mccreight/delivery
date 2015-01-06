package de.fhwedel.delivery.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "PIZZAS")
@PrimaryKeyJoinColumn(name="ID")
public class Pizza extends Product {
    private List<Ingredient> ingredients;

    private Pizza() {
    }

    public Pizza(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public static Pizza empty() {
        return new Pizza(new ArrayList<Ingredient>());
    }

    //TODO: liste unnoetig. reihenfolge nicht relevant. set/multiset? (eher multiset wg. doppelten zutaten?)
    @Column(nullable = false)
    @ManyToMany
    @OrderColumn(name="ingredients_index")
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

    public Pizza addIngredients(Ingredient... toAdd) {
        Collections.addAll(this.ingredients, toAdd);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Pizza.class).addValue(this.getId()).addValue(this.ingredients).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Pizza that = (Pizza) obj;

        return Objects.equal(this.getId(), that.getId())
                && Objects.equal(this.ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId(), this.ingredients);
    }

    @Override
    public BigDecimal evaluateCost() {
        BigDecimal sum = new BigDecimal("0");
        for (Ingredient ingredient : ingredients) {
            sum = sum.add(ingredient.getCost());
        }
        return sum;
    }
}
