import type { WeatherData } from "../types/types";
import { apiClient } from "./client";

export async function getWeatherData(): Promise<WeatherData> {
  return apiClient<WeatherData>("/api/weather");
}
