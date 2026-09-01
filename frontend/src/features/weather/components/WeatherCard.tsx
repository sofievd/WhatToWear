import type {WeatherData} from "../types/types";

interface WeatherCardProps {
    weather: WeatherData;
}
export function WeatherCard({weather}: WeatherCardProps) {
    return(
    <section aria-label="Current weather">
        <h2>{weather.temperature}°C</h2>
        <p>{weather.feelsLike}°C</p>
        <p>{weather.condition}</p>
        <p>Wind {weather.windSpeed} mps</p>
        <p>Precipitation {weather.precipitation}%</p>
    </section>
    ) 

}