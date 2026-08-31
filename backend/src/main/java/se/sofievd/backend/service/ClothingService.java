package se.sofievd.backend.service;

import org.springframework.stereotype.Service;
import se.sofievd.backend.enitiy.Clothing;
import se.sofievd.backend.repository.ClothingRepository;

import java.util.List;

@Service
public class ClothingService {

    private final ClothingRepository clothingRepository;

    public ClothingService(ClothingRepository clothingRepository) {
        this.clothingRepository = clothingRepository;
    }

    public List<Clothing> getAllClothes(){
        return clothingRepository.findAll();
    }
}
