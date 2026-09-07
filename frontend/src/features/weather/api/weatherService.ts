import type { WeatherData } from "../types/types";
import { apiClient } from "./client";

export function getWeatherData(
  latitude: number,
  longitude: number,
): Promise<WeatherData> {
  return apiClient<WeatherData>(
    `/api/weather?latitude=${latitude}&longitude=${longitude}`,
  );
}
