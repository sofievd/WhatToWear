package se.sofievd.backend.service;

import se.sofievd.backend.dto.ClothingDto;
import se.sofievd.backend.enitiy.Clothing;

public class ClothingMapper {

    public static ClothingDto toDto(Clothing clothing){
        return new ClothingDto(clothing.getName(),
                clothing.getCategory(),
                clothing.getCloValue());

    }
}
