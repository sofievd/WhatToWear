package se.sofievd.backend.outfit.model;

import jakarta.persistence.*;

@Entity
@Table(name="clothes")
public class Clothing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item")
    private String name;
    @Column(name = "category")
    private String category;
    @Column(name = "clo-value")
    private Double cloValue;

    public Clothing() {
    }

    public Clothing(String name, String category, Double cloValue, int id) {
        this.name = name;
        this.category = category;
        this.cloValue = cloValue;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getCloValue() {
        return cloValue;
    }

    public void setCloValue(Double cloValue) {
        this.cloValue = cloValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Clothing{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", cloValue=" + cloValue +
                '}';
    }
}
