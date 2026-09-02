import { CircleHelp, ChevronRight } from "lucide-react";

export function WhyThisOutfit() {
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
        <CircleHelp
          size={19}
          className="text-blue-400"
        />

        <span className="text-sm font-bold text-slate-800">
          Why this outfit?
        </span>
      </div>

      <ChevronRight
        size={20}
        className="text-slate-500"
      />
    </button>
  );
}