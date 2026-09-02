import { SlidersHorizontal, ChevronDown } from "lucide-react";

export function PreferencesBar() {
  return (
    <button
      type="button"
      className="
        flex
        h-14
        w-full
        items-center
        justify-between
        rounded-3xl
        border
        border-slate-200
        bg-white
        px-5
        shadow-sm
      "
    >
      <div className="flex items-center gap-3">
        <SlidersHorizontal size={19} className="text-blue-400" />

        <span className="text-sm font-bold text-slate-800">
          Preferences & Sensitivity
        </span>
      </div>

      <div className="flex items-center gap-3">
        <span className="text-sm text-blue-400">Walking · Default</span>

        <ChevronDown size={19} className="text-slate-500" />
      </div>
    </button>
  );
}
