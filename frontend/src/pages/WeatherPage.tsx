import { useEffect, useState } from "react";
import { useAppAuth } from "../auth/AuthContext";
import { AppHeader } from "../components/AppHeader";
import { CitySearch } from "../features/weather/components/CitySearch";
import { OutfitGrid } from "../features/weather/components/OutfitGrid";
import { WhyThisOutfit } from "../features/weather/components/WhyThisOutfit";
import { PreferencesBar } from "../features/weather/components/PreferancesBar";
import {
  getPreferences,
  savePreferences,
} from "../features/weather/api/weatherService";
import type { Preferences } from "../features/weather/components/PreferancesBar";
import { WeatherSection } from "../features/weather/components/WeatherSection";
import { useCitySuggestions } from "../features/weather/hooks/useCitySuggestions";
import type { CitySuggestion } from "../features/weather/types/types";

export function WeatherPage() {
  const { isSignedIn, getToken } = useAppAuth();
  const [accountPreferences, setAccountPreferences] = useState<
    Preferences | undefined
  >();
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

  useEffect(() => {
    if (!isSignedIn) {
      return;
    }

    void getToken()
      .then((token) => {
        if (!token) {
          return;
        }

        return getPreferences(token).then((savedPreferences) => {
          if (savedPreferences) {
            setAccountPreferences(savedPreferences);
          }
        });
      })
      .catch(() => undefined);
  }, [getToken, isSignedIn]);

  async function handlePreferencesSave(preferences: Preferences) {
    if (!isSignedIn) {
      return;
    }

    const token = await getToken();
    if (token) {
      await savePreferences(preferences, token);
    }
  }

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

          <PreferencesBar
            key={
              isSignedIn
                ? accountPreferences
                  ? "account-loaded"
                  : "account-empty"
                : "guest"
            }
            initialPreferences={isSignedIn ? accountPreferences : undefined}
            onSave={(preferences) => void handlePreferencesSave(preferences)}
          />

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
