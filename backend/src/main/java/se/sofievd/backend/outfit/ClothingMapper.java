package se.sofievd.backend.outfit;

public class ClothingMapper {

    public static ClothingDto toDto(Clothing clothing){
        return new ClothingDto(clothing.getName(),
                clothing.getCategory(),
            clothing.getCloValue(),
            clothing.getImageUrl());

    }
}
