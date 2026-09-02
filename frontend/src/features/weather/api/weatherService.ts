import type { WeatherData } from "../types/types";
import { mockWeather } from "./mockWeather";

export async function getWeatherData(): Promise<WeatherData> {
  return mockWeather;
}
