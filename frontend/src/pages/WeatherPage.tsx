import { useState } from "react";
import { WeatherSection } from "../features/weather/components/WeatherSection";
import WearWiseLogo from "../assets/logo.png";

export function WeatherPage() {
  const [location, setLocation] = useState("");
  function handleSearch() {
    const trimmedLocation = location.trim();
    if (!trimmedLocation) {
      return;
    }
    console.log("Searching weather for: ", trimmedLocation);
  }

  return (
    <main className="weather-page">
      <div className="app-header">
        <img src={WearWiseLogo} alt="WearWise" className="logo" />
      </div>
      <section className="weather-card">
        <h1>Check the weather</h1>

        <p className="weather-description">
          Enter a location to get the current weather.
        </p>

        <div className="weather-search">
          <label htmlFor="location">Location</label>

          <input
            id="location"
            type="text"
            value={location}
            onChange={(event) => setLocation(event.target.value)}
            placeholder="Enter a city"
            autoComplete="off"
          />

          <button
            type="button"
            onClick={handleSearch}
            disabled={!location.trim()}
          >
            Get Weather
          </button>
        </div>
      </section>

      <WeatherSection />
    </main>
  );
}
