package se.sofievd.backend.outfit.model;

import java.util.List;

public class Outfit {
    Clothing base;
    Clothing bottom;
    Clothing mid;
    Clothing outer;
    List<Clothing> accessories;
    Clothing footwear;
    Clothing socks;

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

    public List<Clothing> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Clothing> accessories) {
        this.accessories = accessories;
    }

    public Clothing getFootwear() {
        return footwear;
    }

    public void setFootwear(Clothing footwear) {
        this.footwear = footwear;
    }

    public Clothing getSocks() {
        return socks;
    }

    public void setSocks(Clothing socks) {
        this.socks = socks;
    }
}
