package se.sofievd.backend.outfit.model;

public class OutfitRecommendation {
    private  Double targetCloValue;
    private Clothing baseLayer;
    private Clothing midLayer;
    private Clothing outerLayer;
    private Clothing bottom;
    private Clothing footwear;
    private Clothing accessory;

    public OutfitRecommendation(Double targetCloValue) {
        this.targetCloValue = targetCloValue;
    }

    public OutfitRecommendation() {
    }

    public Double getTargetCloValue() {
        return targetCloValue;
    }

    public Clothing getBaseLayer() {
        return baseLayer;
    }

    public void setBaseLayer(Clothing baseLayer) {
        this.baseLayer = baseLayer;
    }

    public Clothing getMidLayer() {
        return midLayer;
    }

    public void setMidLayer(Clothing midLayer) {
        this.midLayer = midLayer;
    }

    public Clothing getOuterLayer() {
        return outerLayer;
    }

    public void setOuterLayer(Clothing outerLayer) {
        this.outerLayer = outerLayer;
    }

    public Clothing getBottom() {
        return bottom;
    }

    public void setBottom(Clothing bottom) {
        this.bottom = bottom;
    }

    public Clothing getFootwear() {
        return footwear;
    }

    public void setFootwear(Clothing footwear) {
        this.footwear = footwear;
    }

    public Clothing getAccessory() {
        return accessory;
    }

    public void setAccessory(Clothing accessory) {
        this.accessory = accessory;
    }

    @Override
    public String toString() {
        return "OutfitRecommendation{" +
                "targetCloValue=" + targetCloValue +
                ", baseLayer=" + baseLayer +
                ", midLayer=" + midLayer +
                ", outerLayer=" + outerLayer +
                ", bottom=" + bottom +
                ", footwear=" + footwear +
                ", accessory=" + accessory +
                '}';
    }
}
