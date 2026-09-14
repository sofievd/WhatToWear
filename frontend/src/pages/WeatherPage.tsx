import { useEffect, useState } from "react";
import { useAppAuth } from "../auth/AuthContext";
import { AppHeader } from "../components/AppHeader";
import { CitySearch } from "../features/weather/components/CitySearch";
import { OutfitGrid } from "../features/weather/components/OutfitGrid";
import { WhyThisOutfit } from "../features/weather/components/WhyThisOutfit";
import {
  getPreferences,
  savePreferences,
} from "../features/weather/api/weatherService";
import { WeatherSection } from "../features/weather/components/WeatherSection";
import { useCitySuggestions } from "../features/weather/hooks/useCitySuggestions";
import type { CitySuggestion } from "../features/weather/types/types";
import type { OutfitRecommendation } from "../features/weather/types/types";
import {
  PreferencesBar,
  type Preferences,
} from "../features/weather/components/PreferancesBar";

const RECENT_CITIES_KEY = "wearwise-recent-cities";
const MAX_RECENT_CITIES = 5;

function getRecentCities(): CitySuggestion[] {
  const stored = localStorage.getItem(RECENT_CITIES_KEY);

  if (!stored) {
    return [];
  }

  try {
    return JSON.parse(stored);
  } catch {
    return [];
  }
}

function saveRecentCity(city: CitySuggestion) {
  const recentCities = getRecentCities();

  const updatedCities = [
    city,
    ...recentCities.filter(
      (item) =>
        item.latitude !== city.latitude || item.longitude !== city.longitude,
    ),
  ].slice(0, MAX_RECENT_CITIES);

  localStorage.setItem(RECENT_CITIES_KEY, JSON.stringify(updatedCities));
}

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
  const [recommendationRequest, setRecommendationRequest] = useState(0);
  const [recommendation, setRecommendation] =
    useState<OutfitRecommendation | null>(null);
  const [recentCities, setRecentCities] =
    useState<CitySuggestion[]>(getRecentCities());

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
    saveRecentCity(city);
    setRecentCities(getRecentCities());

    setSelectedCity(city);
    setSearchValue(city.name);
    setSubmittedCity(city);
    setRecommendation(null);
  }

  function handleSearch() {
    const cityToSubmit = selectedCity ?? suggestions[0];

    if (!cityToSubmit) {
      return;
    }

    saveRecentCity(cityToSubmit);
    setRecentCities(getRecentCities());

    setSelectedCity(cityToSubmit);
    setSearchValue(cityToSubmit.name);
    setSubmittedCity(cityToSubmit);
    setRecommendation(null);
  }

  return (
<main className="min-h-screen bg-[#FCFBF8] px-4 pb-8 pt-2">
      <div className="mx-auto w-full max-w-[390px]">
        <AppHeader />

<div className="space-y-5">
            <CitySearch
            value={searchValue}
            onChange={handleCityChange}
            suggestions={selectedCity ? [] : suggestions}
            recentCities={recentCities}
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

          <button
            type="button"
            onClick={() => {
              setRecommendation(null);
              setRecommendationRequest((current) => current + 1);
            }}
            disabled={!submittedCity}
            className="flex min-h-12 w-full items-center justify-center rounded-2xl bg-[#E77D72] px-4 text-sm font-bold text-white shadow-[0_8px_18px_rgba(231,125,114,0.2)] transition-colors hover:bg-[#D96F65] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#E77D72] focus-visible:ring-offset-2"
          >
            Get my outfit
          </button>

          <section className="pt-1">
            <p className="text-[11px] font-bold uppercase tracking-[0.08em] text-[#E77D72]">
              Your style, today
            </p>

            <h1 className="mt-1 font-serif text-[30px] leading-tight text-[#17294D]">
              What should you wear?
            </h1>

            <p className="mt-1 text-sm leading-relaxed text-[#6F7480]">
              Based on today's weather and your preferences.
            </p>
          </section>

          <OutfitGrid
            requestKey={recommendationRequest}
            onRecommendationChange={setRecommendation}
          />

          <WhyThisOutfit
            recommendation={recommendation}
            latitude={submittedCity?.latitude ?? null}
            longitude={submittedCity?.longitude ?? null}
            preferences={accountPreferences}
          />
        </div>
      </div>
    </main>
  );
}
