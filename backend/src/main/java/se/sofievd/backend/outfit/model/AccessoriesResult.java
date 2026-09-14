package se.sofievd.backend.outfit.model;

import java.util.List;

public record AccessoriesResult(
        List<Clothing> worn,
        List<Clothing> packed
) {
}
