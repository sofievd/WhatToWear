import { Search } from "lucide-react";
import type { CitySuggestion } from "../types/types";

interface CitySearchProps {
  value: string;
  suggestions: CitySuggestion[];
  hasSelectedCity: boolean;
  isLoading: boolean;
  isError: boolean;
  onChange: (value: string) => void;
  onSelectSuggestion: (city: CitySuggestion) => void;
  onSearch: () => void;
}

export function CitySearch({
  value,
  suggestions,
  hasSelectedCity,
  isLoading,
  isError,
  onChange,
  onSelectSuggestion,
  onSearch,
}: CitySearchProps) {
  return (
    <form
      onSubmit={(event) => {
        event.preventDefault();
        onSearch();
      }}
      className="relative"
    >
      <Search
        className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
        size={18}
      />

      <input
        type="search"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder="Search city..."
        aria-label="Search for a city"
        autoComplete="off"
        className="w-full rounded-xl border border-gray-200 bg-white py-3 pl-10 pr-4 outline-none"
      />

      {isLoading && <p className="mt-2 text-sm text-gray-500">Searching...</p>}

      {isError && (
        <p className="mt-2 text-sm text-red-500">Couldn't load cities.</p>
      )}

      {!isLoading && !isError && suggestions.length > 0 && (
        <ul className="absolute z-10 mt-1 max-h-64 w-full overflow-y-auto rounded-xl border border-gray-200 bg-white shadow-lg">
          {suggestions.map((city) => (
            <li key={`${city.name}-${city.latitude}-${city.longitude}`}>
              <button
                type="button"
                onClick={() => onSelectSuggestion(city)}
                className="flex w-full flex-col px-4 py-3 text-left hover:bg-gray-50"
              >
                <span className="font-medium">{city.name}</span>
                <span className="text-sm text-gray-500">{city.country}</span>
              </button>
            </li>
          ))}
        </ul>
      )}

      {!isLoading &&
        !isError &&
        !hasSelectedCity &&
        value.trim().length >= 2 &&
        suggestions.length === 0 && (
          <p className="mt-2 text-sm text-gray-500">No cities found.</p>
        )}
    </form>
  );
}
