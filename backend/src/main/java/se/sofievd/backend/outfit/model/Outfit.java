package se.sofievd.backend.outfit.model;

import java.util.List;

public class Outfit {
    Clothing base;
    Clothing bottom;
    Clothing mid;
    Clothing outer;
    List<Clothing> acessories;
    Clothing footwear;

    public Outfit() {
    }

    public Clothing getBase() {
        return base;
    }

    public void setBase(Clothing base) {
        this.base = base;
    }

    public Clothing getBottom() {
        return bottom;
    }

    public void setBottom(Clothing bottom) {
        this.bottom = bottom;
    }

    public Clothing getMid() {
        return mid;
    }

    public void setMid(Clothing mid) {
        this.mid = mid;
    }

    public Clothing getOuter() {
        return outer;
    }

    public void setOuter(Clothing outer) {
        this.outer = outer;
    }

    public List<Clothing> getAcessories() {
        return acessories;
    }

    public void setAcessories(List<Clothing> acessories) {
        this.acessories = acessories;
    }

    public Clothing getFootwear() {
        return footwear;
    }

    public void setFootwear(Clothing footwear) {
        this.footwear = footwear;
    }
}
