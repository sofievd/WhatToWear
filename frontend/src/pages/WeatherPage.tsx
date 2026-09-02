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
    // Updated background color to #FCFBF8 to match the logo background perfectly
    <main className="min-h-screen bg-[#FCFBF8] px-4 pb-8 pt-4">
      <div className="mx-auto w-full max-w-[390px]">
        <AppHeader />

        <div className="space-y-6">
          <CitySearch
            value={location}
            onChange={setLocation}
            onSearch={handleSearch}
          />

          {data && <CurrentWeather weather={data} city={location} />}

          <PreferencesBar />

          <section className="pt-2">
            <p className="text-[11px] font-bold uppercase tracking-[0.08em] text-[#E77D72]">
              Today's selection
            </p>

            <h1 className="mt-1 font-serif text-[32px] leading-tight text-[#17294D]">
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