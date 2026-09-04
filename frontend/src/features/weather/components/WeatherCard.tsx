import {
  Cloud,
  CloudDrizzle,
  CloudFog,
  CloudLightning,
  CloudRain,
  CloudSnow,
  CloudSun,
  Snowflake,
  Sun,
} from "lucide-react";
import type { LucideIcon } from "lucide-react";
import type { WeatherData } from "../types/types";

interface WeatherCardProps {
  weather: WeatherData;
  city: string;
}

const weatherSymbols: Record<number, { label: string; icon: LucideIcon }> = {
  1: { label: "Clear sky", icon: Sun },
  2: { label: "Nearly clear sky", icon: CloudSun },
  3: { label: "Variable cloudiness", icon: CloudSun },
  4: { label: "Halfclear sky", icon: CloudSun },
  5: { label: "Cloudy sky", icon: Cloud },
  6: { label: "Overcast", icon: Cloud },
  7: { label: "Fog", icon: CloudFog },
  8: { label: "Light rain showers", icon: CloudDrizzle },
  9: { label: "Moderate rain showers", icon: CloudRain },
  10: { label: "Heavy rain showers", icon: CloudRain },
  11: { label: "Thunderstorm", icon: CloudLightning },
  12: { label: "Light sleet showers", icon: CloudRain },
  13: { label: "Moderate sleet showers", icon: CloudRain },
  14: { label: "Heavy sleet showers", icon: CloudRain },
  15: { label: "Light snow showers", icon: CloudSnow },
  16: { label: "Moderate snow showers", icon: CloudSnow },
  17: { label: "Heavy snow showers", icon: CloudSnow },
  18: { label: "Light rain", icon: CloudDrizzle },
  19: { label: "Moderate rain", icon: CloudRain },
  20: { label: "Heavy rain", icon: CloudRain },
  21: { label: "Thunder", icon: CloudLightning },
  22: { label: "Light sleet", icon: CloudRain },
  23: { label: "Moderate sleet", icon: CloudRain },
  24: { label: "Heavy sleet", icon: CloudRain },
  25: { label: "Light snowfall", icon: Snowflake },
  26: { label: "Moderate snowfall", icon: CloudSnow },
  27: { label: "Heavy snowfall", icon: CloudSnow },
};

export function WeatherCard({ weather, city }: WeatherCardProps) {
  const weatherSymbol = weatherSymbols[weather.condition] ?? {
    label: "Unknown weather",
    icon: Cloud,
  };
  const WeatherIcon = weatherSymbol.icon;

  return (
    <section
      className="weather-display"
      aria-label={`Current weather in ${city}`}
    >
      <div className="weather-heading">
        <div>
          <p className="weather-eyebrow">Current weather</p>
          <h2 className="weather-city">{city}</h2>
        </div>
        <div className="weather-icon" aria-hidden="true">
          <WeatherIcon size={28} />
        </div>
      </div>

      <div className="temp-main">
        <h2 className="temp-value">{weather.temperature}°C</h2>
        <p className="temp-feels">Feels like {(weather.feelsLike).toFixed(1)}°C</p>
      </div>

      <div className="weather-details">
        <div className="detail-item">
          <span className="detail-label">Condition</span>
          <span className="detail-value">
            {weather.conditionDescription || weatherSymbol.label}
          </span>
        </div>
        <div className="detail-item">
          <span className="detail-label">Wind </span>
          <span className="detail-value whitespace-nowrap">
            {(weather.windSpeed).toFixed(1)} km/h
          </span>
        </div>
        <div className="detail-item">
          <span className="detail-label">Precipitation </span>
          <span className="detail-value">
            {weather.precipitation} mm
            <span className="text-italic">
              {weather.precipitationDescription}
            </span>
          </span>
        </div>
      </div>
    </section>
  );
}
