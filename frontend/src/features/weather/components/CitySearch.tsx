import { useEffect, useRef, useState } from "react";
import { Search } from "lucide-react";
import type { CitySuggestion } from "../types/types";

interface CitySearchProps {
  value: string;
  suggestions: CitySuggestion[];
  recentCities: CitySuggestion[];
  isLoading: boolean;
  isError: boolean;
  onChange: (value: string) => void;
  onSelectSuggestion: (city: CitySuggestion) => void;
  onSearch: () => void;
}

export function CitySearch({
  value,
  suggestions,
  recentCities,
  isLoading,
  isError,
  onChange,
  onSelectSuggestion,
  onSearch,
}: CitySearchProps) {
  const [isFocused, setIsFocused] = useState(false);
  const searchRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (
        searchRef.current &&
        !searchRef.current.contains(event.target as Node)
      ) {
        setIsFocused(false);
      }
    }

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const showDropdown = isFocused;

  return (
    <div ref={searchRef} className="relative">
      <div className="flex items-center rounded-2xl border border-gray-200 bg-white px-4 shadow-sm">
        <Search size={19} className="text-gray-400" />

        <input
          value={value}
          onChange={(event) => onChange(event.target.value)}
          onFocus={() => setIsFocused(true)}
          onKeyDown={(event) => {
            if (event.key === "Enter") {
              onSearch();
              setIsFocused(false);
            }
          }}
          placeholder="Search for a city"
          className="w-full bg-transparent px-3 py-4 text-sm text-gray-800 outline-none placeholder:text-gray-400"
        />
      </div>

      {showDropdown && (
        <div className="absolute left-0 right-0 top-full z-20 mt-2 overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-lg">
          {/* Recent searches — only when nothing has been typed */}
          {value.trim() === "" && recentCities.length > 0 && (
            <>
              <div className="px-4 py-3">
                <p className="text-xs font-bold uppercase tracking-wide text-gray-400">
                  Recent searches
                </p>
              </div>

              <div className="border-t border-gray-100">
                {recentCities.map((city) => (
                  <button
                    key={`${city.name}-${city.latitude}-${city.longitude}`}
                    type="button"
                    onClick={() => {
                      onSelectSuggestion(city);
                      setIsFocused(false);
                    }}
                    className="w-full px-4 py-3 text-left transition-colors hover:bg-gray-50"
                  >
                    <p className="font-medium text-gray-800">{city.name}</p>
                    <p className="text-sm text-gray-500">{city.country}</p>
                  </button>
                ))}
              </div>
            </>
          )}

          {/* Normal search suggestions */}
          {value.trim() !== "" && suggestions.length > 0 && (
            <div>
              {suggestions.map((city) => (
                <button
                  key={`${city.name}-${city.latitude}-${city.longitude}`}
                  type="button"
                  onClick={() => {
                    onSelectSuggestion(city);
                    setIsFocused(false);
                  }}
                  className="w-full px-4 py-3 text-left transition-colors hover:bg-gray-50"
                >
                  <p className="font-medium text-gray-800">{city.name}</p>
                  <p className="text-sm text-gray-500">{city.country}</p>
                </button>
              ))}
            </div>
          )}

          {isLoading && value.trim() !== "" && (
            <p className="px-4 py-3 text-sm text-gray-400">
              Searching...
            </p>
          )}

          {isError && (
            <p className="px-4 py-3 text-sm text-red-500">
              Could not load cities.
            </p>
          )}
        </div>
      )}
    </div>
  );
}