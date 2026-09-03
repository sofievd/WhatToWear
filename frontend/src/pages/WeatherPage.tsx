import { useState } from "react";
import { AppHeader } from "../components/AppHeader";
import { CitySearch } from "../features/weather/components/CitySearch";
import { OutfitGrid } from "../features/weather/components/OutfitGrid";
import { WhyThisOutfit } from "../features/weather/components/WhyThisOutfit";
import { PreferencesBar } from "../features/weather/components/PreferancesBar";
import { WeatherSection } from "../features/weather/components/WeatherSection";

export function WeatherPage() {
  const [searchValue, setSearchValue] = useState("");
  const [city, setCity] = useState("");

  function handleSearch() {
    const trimmedCity = searchValue.trim();

    if (!trimmedCity) {
      return;
    }

    setCity(trimmedCity);
    console.log("Searching weather for:", trimmedCity);
  }

  return (
    // Updated background color to #FCFBF8 to match the logo background perfectly
    <main className="min-h-screen bg-[#FCFBF8] px-4 pb-8 pt-4">
      <div className="mx-auto w-full max-w-[390px]">
        <AppHeader />

        <div className="space-y-6">
          <CitySearch
            value={searchValue}
            onChange={setSearchValue}
            onSearch={handleSearch}
          />

          <WeatherSection city={city} />

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
