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
  imageUrl?: string;
}

export interface OutfitResponse {
  base: ClothingItem | null;
  bottom: ClothingItem | null;
  footwear: ClothingItem | null;
  socks: ClothingItem | null;
  mid: ClothingItem | null;
  outer: ClothingItem | null;
  accessories: ClothingItem[];
}

export interface OutfitRecommendation {
  wearNow: OutfitResponse;
  packInBag: ClothingItem[];
}

export interface OutfitExplanation {
  explanation: string;
}
