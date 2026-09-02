package se.sofievd.backend.service;

import org.springframework.stereotype.Service;
import se.sofievd.backend.client.SmhiClient;
import se.sofievd.backend.data.smhi.SmhiWeather;
import se.sofievd.backend.enitiy.Clothing;
import se.sofievd.backend.repository.ClothingRepository;

import java.util.List;

@Service
public class ClothingService {

    private final ClothingRepository clothingRepository;
    private final SmhiClient client;

    public ClothingService(ClothingRepository clothingRepository, SmhiClient client) {
        this.clothingRepository = clothingRepository;
        this.client = client;
    }

    public List<Clothing> getAllClothes(){
        return clothingRepository.findAll();
    }

    public String getWeather(){
        SmhiWeather weather=  client.getForecast(59.334591, 18.063240);
        System.out.println(weather);
        return "weather: ";
    }

}
