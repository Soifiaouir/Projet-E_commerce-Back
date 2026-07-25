package fr.soifi.ecommerce.dal.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
@Entity
@Table (name = "category") //nom de la table
public class Category {
    @Id// clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrémenation
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;



    public Category() {
    }

    public Category(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
