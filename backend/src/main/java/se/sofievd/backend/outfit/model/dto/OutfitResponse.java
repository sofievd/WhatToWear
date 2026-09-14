package se.sofievd.backend.outfit.model.dto;

import se.sofievd.backend.outfit.model.Clothing;

import java.util.List;

public record OutfitResponse(Clothing base,
                             Clothing bottom,
                             Clothing mid,
                             Clothing outer,
                             List<Clothing> accessories,
                             Clothing footwear,
                             Clothing socks) {
}
