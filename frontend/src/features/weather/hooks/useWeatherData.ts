import { useQuery } from "@tanstack/react-query";
import { getWeatherData } from "../api/weatherService";

export function useWeather(
  latitude: number | null,
  longitude: number | null,
) {
  return useQuery({
    queryKey: ["weather", latitude, longitude],

    queryFn: () => getWeatherData(latitude!, longitude!),

    enabled: latitude !== null && longitude !== null,
  });
}
