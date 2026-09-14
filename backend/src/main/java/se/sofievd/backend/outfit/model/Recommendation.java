package se.sofievd.backend.outfit.model;

import java.util.List;

public class Recommendation {
    Outfit wearNow;
    List<Clothing> packInBag;

    public Outfit getWearNow() {
        return wearNow;
    }

    public void setWearNow(Outfit wearNow) {
        this.wearNow = wearNow;
    }

    public List<Clothing> getPackInBag() {
        return packInBag;
    }

    public void setPackInBag(List<Clothing> packInBag) {
        this.packInBag = packInBag;
    }
}
