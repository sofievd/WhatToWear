import {useState} from "react";

export function WeatherPage(){

    const [location, setLocation] = useState("");
    function handleSearch(){
        const trimmedLocation = location.trim();
        if (!trimmedLocation){
            return;
        }
        console.log("Searching weather for: ",trimmedLocation)
    }

    return (
        <main className="weather-page">
            <section className="weather-card">
                <p className="weather-eyebrow">Weather</p>

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
        </main>
    );
}