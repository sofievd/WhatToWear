import type { WeatherData } from "../types/types";
import { apiClient } from "./client";
import type { Preferences } from "../components/PreferancesBar";

export interface PreferencesResponse {
  preferences: Preferences;
  availableClothing: Array<{
    name: string;
    category: string;
    cloValue: number;
  }>;
}

export function getWeatherData(
  latitude: number,
  longitude: number,
): Promise<WeatherData> {
  return apiClient<WeatherData>(
    `/api/weather?latitude=${latitude}&longitude=${longitude}`,
  );
}

export function savePreferences(
  preferences: Preferences,
): Promise<PreferencesResponse> {
  return apiClient<PreferencesResponse>("/api/preferences", {
    method: "POST",
    body: JSON.stringify(preferences),
  });
}
