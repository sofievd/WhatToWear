package se.sofievd.backend.enitiy;


public class Clothing {
    private  String name;
    private  String category;
    private  Double cloValue;
    private int id;

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
}
