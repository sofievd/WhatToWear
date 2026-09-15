import type {
  ClothingItem,
  OutfitRecommendation,
  OutfitExplanation,
  WeatherData,
} from "../types/types";
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

export function getRecommendation(
  latitude: number,
  longitude: number,
  toleranceLevel: string,
  activityLevel: string,
): Promise<OutfitRecommendation> {
  return apiClient<OutfitRecommendation>(
    `/api/recommendation?latitude=${latitude}&longitude=${longitude}&toleranceLevel=${encodeURIComponent(
      toleranceLevel,
    )}&activityLevel=${encodeURIComponent(activityLevel)}`,
  );
}

export function getOutfitExplanation(request: {
  latitude: number;
  longitude: number;
  recommendation: OutfitRecommendation;
  preferences: Preferences;
}): Promise<OutfitExplanation> {
  return apiClient<OutfitExplanation>("/api/recommendation/explanation", {
    method: "POST",
    body: JSON.stringify(request),
  });
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
