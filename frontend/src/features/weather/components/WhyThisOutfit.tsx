import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { CircleHelp, ChevronDown, ChevronRight } from "lucide-react";
import { getOutfitExplanation } from "../api/weatherService";
import type { OutfitExplanation, OutfitRecommendation } from "../types/types";
import type { Preferences } from "./PreferancesBar";

const defaultPreferences: Preferences = {
  activity: "walking",
  thermalSensitivity: "neutral",
};

export function WhyThisOutfit({
  recommendation,
  latitude,
  longitude,
  preferences,
}: {
  recommendation: OutfitRecommendation | null;
  latitude: number | null;
  longitude: number | null;
  preferences?: Preferences;
}) {
  const [isOpen, setIsOpen] = useState(false);
  const [explanation, setExplanation] = useState<OutfitExplanation | null>(
    null,
  );

  const mutation = useMutation({
    mutationFn: () =>
      getOutfitExplanation({
        latitude: latitude!,
        longitude: longitude!,
        recommendation: recommendation!,
        preferences: preferences ?? loadGuestPreferences(),
      }),
    onSuccess: (data) => {
      setExplanation(data);
    },
  });

  function handleOpen() {
    const canExplain =
      recommendation !== null && latitude !== null && longitude !== null;

    if (!canExplain) {
      return;
    }

    const opening = !isOpen;

    setIsOpen(opening);

    // Only request an explanation when the user opens the section.
    if (opening) {
      setExplanation(null);
      mutation.mutate();
    }
  }

  return (
    <section className="space-y-3">
      <button
        type="button"
        onClick={handleOpen}
        disabled={!recommendation || latitude === null || longitude === null}
        aria-expanded={isOpen}
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
          disabled:cursor-not-allowed
          disabled:opacity-50
        "
      >
        <div className="flex items-center gap-3">
          <CircleHelp size={19} className="text-blue-400" />

          <span className="text-sm font-bold text-slate-800">
            Why this outfit?
          </span>
        </div>

        {isOpen ? (
          <ChevronDown size={20} className="text-slate-500" />
        ) : (
          <ChevronRight size={20} className="text-slate-500" />
        )}
      </button>

      {isOpen && (
        <div className="space-y-2 rounded-3xl border border-slate-200 bg-white p-5 text-sm leading-6 text-slate-600 shadow-sm">
          {mutation.isPending && <p>Preparing your outfit explanation...</p>}

          {mutation.isError && (
            <p role="alert">
              {mutation.error instanceof Error &&
              mutation.error.message.includes("usage limit")
                ? "OpenRouter usage limit reached. Please try again later."
                : mutation.error instanceof Error &&
                    mutation.error.message.includes("needs more credits")
                  ? "OpenRouter needs more credits for this request."
                  : mutation.error instanceof Error &&
                      mutation.error.message.includes("OpenRouter rejected")
                    ? mutation.error.message
                    : "We could not explain this outfit right now."}
            </p>
          )}

          {explanation && (
            <p className="text-slate-600">{explanation.explanation}</p>
          )}
        </div>
      )}
    </section>
  );
}

function loadGuestPreferences(): Preferences {
  const storedPreferences = localStorage.getItem("wearwise-preferences");

  if (!storedPreferences) {
    return defaultPreferences;
  }

  try {
    return {
      ...defaultPreferences,
      ...JSON.parse(storedPreferences),
    };
  } catch {
    return defaultPreferences;
  }
}
