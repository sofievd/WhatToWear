export interface WeatherData {
  temperature: number;
  feelsLike: number;
  condition: string;
  windSpeed: number;
  precipitation: number;
  precipitationDescription?: string;
}
