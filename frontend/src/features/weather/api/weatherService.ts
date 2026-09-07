import type { ClothingItem, WeatherData } from "../types/types";
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

export function getClothing(): Promise<ClothingItem[]> {
  return apiClient<ClothingItem[]>("/api/weather/clothes");
}

export function savePreferences(
  preferences: Preferences,
  token: string,
): Promise<PreferencesResponse> {
  return apiClient<PreferencesResponse>("/api/preferences", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(preferences),
  });
}

export function getPreferences(token: string): Promise<Preferences | null> {
  return apiClient<Preferences | null>("/api/preferences", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
}
