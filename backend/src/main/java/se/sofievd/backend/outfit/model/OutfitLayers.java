package se.sofievd.backend.outfit.model;

public record OutfitLayers(
        Clothing footwear,
        Clothing socks,
        Clothing bottom,
        Clothing base,
        Clothing midLayer,
        Clothing outerLayer
) {
}
