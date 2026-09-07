package se.sofievd.backend.outfit;

import jakarta.persistence.*;

@Entity
@Table(name="clothes")
public class Clothing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item")
    private String name;
    private String category;
    @Column(name = "clo-value")
    private Double cloValue;

    @Column(name = "image_url")
    private String imageUrl;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
