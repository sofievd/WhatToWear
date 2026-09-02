import { useState } from "react";
import { AppHeader } from "../components/AppHeader";
import { CitySearch } from "../features/weather/components/CitySearch";
import { CurrentWeather } from "../features/weather/components/CurrentWeather";
import { OutfitGrid } from "../features/weather/components/OutfitGrid";
import { WhyThisOutfit } from "../features/weather/components/WhyThisOutfit";
import { useWeather } from "../features/weather/hooks/useWeatherData";
import { PreferencesBar } from "../features/weather/components/PreferancesBar";

export function WeatherPage() {
  const [location, setLocation] = useState("San Francisco");

  const { data } = useWeather();

  function handleSearch() {
    const trimmedLocation = location.trim();

    if (!trimmedLocation) {
      return;
    }

    console.log("Searching weather for:", trimmedLocation);
  }

  return (
    <main className="min-h-screen bg-[#F7F5F0] px-4 pb-10">
      <div className="mx-auto w-full max-w-md">
        <AppHeader />

        <div className="space-y-5">
          <CitySearch
            value={location}
            onChange={setLocation}
            onSearch={handleSearch}
          />

          {data && (
            <CurrentWeather
              weather={data}
              city={location}
            />
          )}

          <PreferencesBar />

          <section className="pt-1">
            <p className="text-xs font-medium uppercase tracking-wide text-[#E77D72]">
              Today's selection
            </p>

            <h1 className="mt-2 font-serif text-[30px] leading-tight text-[#17294D]">
              Your Recommended Outfit
            </h1>
          </section>

          <OutfitGrid />

          <WhyThisOutfit />
        </div>
      </div>
    </main>
  );
}