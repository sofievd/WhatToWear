import { Search } from "lucide-react";

interface CitySearchProps {
  value: string;
  onChange: (value: string) => void;
  onSearch: () => void;
}

export function CitySearch({
  value,
  onChange,
  onSearch,
}: CitySearchProps) {
  return (
    <div className="relative">
      <Search
        size={21}
        strokeWidth={2}
        className="absolute left-5 top-1/2 -translate-y-1/2 text-slate-500"
      />

      <input
        type="text"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        onKeyDown={(event) => {
          if (event.key === "Enter") {
            onSearch();
          }
        }}
        placeholder="Search city..."
        autoComplete="on"
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
    </div>
  );
}