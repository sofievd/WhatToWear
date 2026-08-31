package se.sofievd.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.dto.ClothingDto;
import se.sofievd.backend.enitiy.Clothing;
import se.sofievd.backend.service.ClothingMapper;
import se.sofievd.backend.service.ClothingService;

import java.util.List;

@RestController
public class Controller {
    private final ClothingService clothingService;

    public Controller(ClothingService clothingService) {
        this.clothingService = clothingService;
    }

    @GetMapping("/api/weather/clothes")
    public ResponseEntity<List<ClothingDto>> getClothes(){
        List<Clothing> clothes = clothingService.getAllClothes();
        return ResponseEntity.ok().body(clothes.stream().map(ClothingMapper::toDto).toList());
    }

}
