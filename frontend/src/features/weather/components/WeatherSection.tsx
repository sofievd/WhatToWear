import { useWeather } from "../hooks/useWeatherData";
import { WeatherCard } from "./WeatherCard";
import { WeatherCardSkeleton } from "./WeatherCardSkeleton";
import { WeatherError } from "./WeatherError";

export function WeatherSection() {
  const { data, isLoading, error, retry } = useWeather();

  if (isLoading) {
    return <WeatherCardSkeleton />;
  }

  if (error) {
    return <WeatherError message={error} onRetry={retry} />;
  }

  if (!data) {
    return null;
  }

  return <WeatherCard weather={data} />;
}
