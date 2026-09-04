import { Search } from "lucide-react";
import type { CitySuggestion } from "../types/types";

interface CitySearchProps {
  value: string;
  suggestions: CitySuggestion[];
  onChange: (value: string) => void;
  onSelectSuggestion: (city: CitySuggestion) => void;
  onSearch: () => void;
}

export function CitySearch({
  value,
  suggestions,
  onChange,
  onSelectSuggestion,
  onSearch,
}: CitySearchProps) {
  const showSuggestions = value.trim().length >= 2;

  return (
    <form
      onSubmit={(event) => {
        event.preventDefault();
        onSearch();
      }}
      className="relative"
    >
      <Search
        size={21}
        strokeWidth={2}
        aria-hidden="true"
        className="pointer-events-none absolute left-5 top-1/2 -translate-y-1/2 text-slate-500"
      />

      <input
        type="search"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder="Search city..."
        aria-label="Search for a city"
        autoComplete="off"
        className="
          h-14
          w-full
          rounded-full
          border
          border-slate-200
          bg-white
          pl-13
          pr-5
          text-base
          text-slate-800
          shadow-sm
          outline-none
          transition
          placeholder:text-slate-400
          focus:border-slate-300
          focus:ring-2
          focus:ring-slate-200
        "
      />
      {showSuggestions && (
        <div className=" absolute z-10 mt-2 w-full overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-lg ">
          {" "}
          {suggestions.length > 0 ? (
            <ul>
              {" "}
              {suggestions.map((city) => (
                <li key={`${city.name}-${city.latitude}-${city.longitude}`}>
                  {" "}
                  <button
                    type="button"
                    onClick={() => onSelectSuggestion(city)}
                    className=" w-full px-5 py-3 text-left transition hover:bg-slate-50 "
                  >
                    {" "}
                    <span className="block font-medium text-slate-800">
                      {" "}
                      {city.name}{" "}
                    </span>{" "}
                    <span className="block text-sm text-slate-500">
                      {" "}
                      {city.country}{" "}
                    </span>{" "}
                  </button>{" "}
                </li>
              ))}{" "}
            </ul>
          ) : (
            <p className="px-5 py-4 text-sm text-slate-500">
              {" "}
              No cities found.{" "}
            </p>
          )}{" "}
        </div>
      )}
    </form>
  );
}
