import { useState } from "react";
import { AppHeader } from "../components/AppHeader";
import { CitySearch } from "../features/weather/components/CitySearch";
import { OutfitGrid } from "../features/weather/components/OutfitGrid";
import { WhyThisOutfit } from "../features/weather/components/WhyThisOutfit";
import { PreferencesBar } from "../features/weather/components/PreferancesBar";
import { savePreferences } from "../features/weather/api/weatherService";
import { WeatherSection } from "../features/weather/components/WeatherSection";
import { useCitySuggestions } from "../features/weather/hooks/useCitySuggestions";
import type { CitySuggestion } from "../features/weather/types/types";

export function WeatherPage() {
  const [searchValue, setSearchValue] = useState("");
  const [selectedCity, setSelectedCity] = useState<CitySuggestion | null>(null);

  // The city that was actually submitted with the Search button.
  const [submittedCity, setSubmittedCity] = useState<CitySuggestion | null>(
    null,
  );

  const {
    data: suggestions = [],
    isLoading: isLoadingSuggestions,
    isError: isSuggestionsError,
  } = useCitySuggestions(searchValue);

  function handleCityChange(value: string) {
    setSearchValue(value);
    setSelectedCity(null);
  }

  function handleCitySelect(city: CitySuggestion) {
    setSelectedCity(city);
    setSearchValue(city.name);
    setSubmittedCity(city);
  }

  function handleSearch() {
    const cityToSubmit = selectedCity ?? suggestions[0];

    if (!cityToSubmit) {
      return;
    }

    setSelectedCity(cityToSubmit);
    setSearchValue(cityToSubmit.name);
    setSubmittedCity(cityToSubmit);
  }

  return (
    <main className="min-h-screen bg-[#FCFBF8] px-4 pb-8 pt-4">
      <div className="mx-auto w-full max-w-[390px]">
        <AppHeader />

        <div className="space-y-6">
          <CitySearch
            value={searchValue}
            onChange={handleCityChange}
            suggestions={selectedCity ? [] : suggestions}
            hasSelectedCity={selectedCity !== null}
            onSelectSuggestion={handleCitySelect}
            onSearch={handleSearch}
            isLoading={isLoadingSuggestions}
            isError={isSuggestionsError}
          />

          <WeatherSection
            city={submittedCity?.name ?? ""}
            latitude={submittedCity?.latitude ?? null}
            longitude={submittedCity?.longitude ?? null}
          />

          <PreferencesBar onSave={(preferences) => void savePreferences(preferences)} />

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
