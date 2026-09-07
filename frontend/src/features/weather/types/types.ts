export interface WeatherData {
  temperature: number;
  feelsLike: number;
  condition: number;
  conditionDescription: string;
  windSpeed: number;
  precipitation: number;
  precipitationDescription: string;
}

export interface CitySuggestion {
  name: string;
  country: string;
  latitude: number;
  longitude: number;
}

export interface ClothingItem {
  name: string;
  category: string;
  cloValue: number;
  imageUrl: string | null;
}