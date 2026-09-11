package se.sofievd.backend.outfit;

public record ClothingDto(int id,
        String name,
                          String category,
                          Double cloValue,
                          String imageUrl) {
}
