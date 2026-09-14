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
      className="rounded-2xl border border-[#E9E5DE] bg-white px-4 py-3 shadow-sm"
      aria-label={`Current weather in ${city}`}
    >
      <div className="flex items-center justify-between gap-3">
        {/* City + weather */}
        <div className="min-w-0">
          <div className="flex items-baseline gap-2">
            <h2 className="truncate text-base font-bold text-[#17294D]">
              {city}
            </h2>

            <span className="text-xl font-bold text-[#17294D]">
              {weather.temperature}°C
            </span>
          </div>

          <p className="mt-0.5 truncate text-xs text-[#6F7480]">
            {weather.conditionDescription || weatherSymbol.label} · Feels like{" "}
            {weather.feelsLike.toFixed(1)}°C
          </p>
        </div>

        {/* Weather icon */}
        <div
          className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-[#FCF4EE] text-[#E77D72]"
          aria-hidden="true"
        >
          <WeatherIcon size={22} />
        </div>
      </div>

      {/* Small supporting details */}
      <div className="mt-2 flex gap-4 border-t border-[#F0ECE6] pt-2 text-[11px] text-[#6F7480]">
        <span>
          Wind{" "}
          <strong className="text-[#17294D]">
            {weather.windSpeed.toFixed(1)} km/h
          </strong>
        </span>

        <span>
          Rain{" "}
          <strong className="text-[#17294D]">{weather.precipitation} mm</strong>
        </span>
      </div>
    </section>
  );
}
