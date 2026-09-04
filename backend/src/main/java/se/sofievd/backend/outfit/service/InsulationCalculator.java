package se.sofievd.backend.outfit.service;

import org.springframework.stereotype.Service;

@Service
public class InsulationCalculator {

    public Double calculateEffectiveTemperature(Double temperature, Double tolerance){
        return temperature - tolerance;
    }

    public Double calculateTargetClo(Double effectiveTemperature, Double metValue, Double windSpeed){

        Double windSpeedMS = (windSpeed / 3.6);
        // convert Met in w/m^2
        Double M = metValue * 58.2;

        Double totalInsulationRequired = (33.0 - effectiveTemperature) / (0.155 * M);

        Double airInsulation = 0.7 - Math.min(0.5, 0.1 * windSpeed);

        Double targetClothingClo = totalInsulationRequired - airInsulation;

        return Math.max(targetClothingClo, 0.15);

    }

}