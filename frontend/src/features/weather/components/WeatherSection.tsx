import { useWeather } from "../hooks/useWeatherData";
import { WeatherCard } from "./WeatherCard";
import { WeatherCardSkeleton } from "./WeatherCardSkeleton";
import { WeatherError } from "./WeatherError";
interface WeatherSectionProps {
  city: string;
  latitude: number | null;
  longitude: number | null;
}
export function WeatherSection({
  city,
  latitude,
  longitude,
}: WeatherSectionProps) {
  const { data, isLoading, isError, refetch } = useWeather(latitude, longitude);
  if (isLoading) {
    return <WeatherCardSkeleton />;
  }
  if (isError) {
    return (
      <WeatherError
        message="We couldn't load the weather right now."
        onRetry={() => void refetch()}
      />
    );
  }
  if (!data) {
    return null;
  }
  return <WeatherCard weather={data} city={city} />;
}
