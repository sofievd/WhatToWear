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
    <section className="weather-display" aria-label="Current weather">
      <div className="temp-main">
        <h2 className="temp-value">{weather.temperature}°C</h2>
        <p className="temp-feels">Feels like {weather.feelsLike}°C</p>
      </div>

      <div className="weather-details">
        <div className="detail-item">
          <span className="detail-label">Condition</span>
          <span className="detail-value">{weather.condition}</span>
        </div>
        <div className="detail-item">
          <span className="detail-label">Wind</span>
          <span className="detail-value">{weather.windSpeed} m/s</span>
        </div>
        <div className="detail-item">
          <span className="detail-label">Precipitation</span>
          <span className="detail-value">
            {weather.precipitation} mm (
            {getRainDescription(weather.precipitation)})
          </span>
        </div>
      </div>
    </section>
  );
}
