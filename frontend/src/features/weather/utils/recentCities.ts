// utils/recentCities.ts

import type { CitySuggestion } from "../types/types";

const STORAGE_KEY = "wearwise-recent-cities";
const MAX_RECENT_CITIES = 5;

export function getRecentCities(): CitySuggestion[] {
  const stored = localStorage.getItem(STORAGE_KEY);

  if (!stored) {
    return [];
  }

  try {
    return JSON.parse(stored);
  } catch {
    return [];
  }
}

export function saveRecentCity(city: CitySuggestion) {
  const current = getRecentCities();

  const updated = [
    city,
    ...current.filter(
      (item) =>
        item.latitude !== city.latitude || item.longitude !== city.longitude,
    ),
  ].slice(0, MAX_RECENT_CITIES);

  localStorage.setItem(STORAGE_KEY, JSON.stringify(updated));
}
