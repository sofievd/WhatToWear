package se.sofievd.backend.outfit.service;

import se.sofievd.backend.outfit.ClothingDto;
import se.sofievd.backend.outfit.model.Clothing;

public class ClothingMapper {

    public static ClothingDto toDto(Clothing clothing){
        return new ClothingDto(clothing.getName(),
                clothing.getCategory(),
                clothing.getCloValue());

    }
}
