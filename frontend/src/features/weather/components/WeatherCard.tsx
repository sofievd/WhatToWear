import type { WeatherData } from "../types/types";

interface WeatherCardProps {
  weather: WeatherData;
}
function getRainDescription(mm: number) {
  if (mm === 0) return "No rain";
  if (mm < 1) return "Very light rain";
  if (mm < 5) return "Light rain";
  if (mm < 10) return "Moderate rain";
  if (mm < 20) return "Heavy rain";
  return "Very heavy rain";
}

export function WeatherCard({ weather }: WeatherCardProps) {
  return (
    <section aria-label="Current weather">
      {" "}
      <h2>Temperature: {weather.temperature}°C</h2>{" "}
      <p>Feels like: {weather.feelsLike}°C</p> <p>{weather.condition}</p>{" "}
      <p>Wind: {weather.windSpeed} m/s</p>{" "}
      <p> Precipitation: {weather.precipitation} mm ({getRainDescription(weather.precipitation)}) </p>{" "}
    </section>
  );
}
