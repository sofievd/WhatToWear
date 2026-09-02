import { CloudSun } from "lucide-react";
import type { WeatherData } from "../types/types";

interface CurrentWeatherProps {
  weather: WeatherData;
  city: string;
}

export function CurrentWeather({ weather, city }: CurrentWeatherProps) {
  return (
    <section className="flex items-center justify-between rounded-3xl border border-slate-200 bg-white px-5 py-4 shadow-sm">
      <div>
        <h2 className="text-base font-bold text-slate-800">{city}</h2>

        <p className="mt-0.5 text-sm text-slate-500">
          Feels like {weather.feelsLike}°C . Wind {weather.windSpeed} km/h
        </p>
      </div>

      <div className="flex items-center gap-3">
        <span className="text-xl font-bold text-slate-800">
          {weather.temperature}°C
        </span>

        <div className="flex items-center gap-2">
          <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-slate-100">
            <CloudSun size={19} className="text-slate-500" />
          </div>

          <span className="text-sm text-slate-500">{weather.condition}</span>
        </div>
      </div>
    </section>
  );
}
