package se.sofievd.backend.outfit;

import org.springframework.stereotype.Service;
import se.sofievd.backend.weather.smhi.SmhiClient;
import se.sofievd.backend.weather.smhi.model.SmhiWeather;

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
