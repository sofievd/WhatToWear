import { useQuery } from "@tanstack/react-query";
import { useEffect } from "react";
import { getRecommendation } from "../api/weatherService";
import { OutfitCard } from "./OutfitCard";
import type { ClothingItem, OutfitRecommendation } from "../types/types";
import type { Preferences } from "./PreferancesBar";

export function OutfitGrid({
  requestKey,
  latitude,
  longitude,
  preferences,
  onRecommendationChange,
}: {
  requestKey: number;
  latitude: number | null;
  longitude: number | null;
  preferences: Preferences;
  onRecommendationChange?: (
    recommendation: OutfitRecommendation | null,
  ) => void;
}) {
  const {
    data: recommendation,
    isLoading,
    isError,
  } = useQuery({
    queryKey: [
      "recommendation",
      requestKey,
      latitude,
      longitude,
      preferences.activity,
      preferences.thermalSensitivity,
    ],
    queryFn: () =>
      getRecommendation(
        latitude!,
        longitude!,
        preferences.thermalSensitivity,
        preferences.activity,
      ),
    enabled: requestKey > 0 && latitude !== null && longitude !== null,
  });

  useEffect(() => {
    onRecommendationChange?.(recommendation ?? null);
  }, [onRecommendationChange, recommendation]);

  const outfit = recommendation?.wearNow;

  const clothing = outfit
    ? [
        outfit.base,
        outfit.mid,
        outfit.outer,
        outfit.bottom,
        outfit.footwear,
        outfit.socks,
        ...outfit.accessories,
      ].filter((item): item is ClothingItem => item !== null)
    : [];

  const packInBag = recommendation?.packInBag ?? [];

  if (requestKey === 0) {
    return (
      <p className="rounded-2xl border border-[#D9E1EE] bg-white p-4 text-sm text-[#667085]">
        Choose your city and preferences, then get a recommendation.
      </p>
    );
  }

  if (isLoading) {
    return (
      <div className="grid grid-cols-2 gap-4" aria-label="Loading clothing">
        {[0, 1, 2, 3].map((item) => (
          <div
            key={item}
            className="h-56 animate-pulse rounded-[20px] bg-[#F0EFEB]"
          />
        ))}
      </div>
    );
  }

  if (isError) {
    return (
      <p className="rounded-2xl border border-[#F0C2BD] bg-[#FFF5F3] p-4 text-sm text-[#A34B43]">
        We could not load clothing recommendations right now.
      </p>
    );
  }

  if (clothing.length === 0) {
    return (
      <p className="rounded-2xl border border-[#D9E1EE] bg-white p-4 text-sm text-[#667085]">
        No clothing is available yet.
      </p>
    );
  }

  return (
    <div className="space-y-7">
      {/* MAIN OUTFIT */}
      <section>
        <div className="mb-3 flex items-end justify-between">
          <div>
            <p className="text-[10px] font-bold uppercase tracking-[0.12em] text-[#E77D72]">
              Wear now
            </p>
            <h2 className="mt-1 font-serif text-[22px] leading-tight text-[#17294D]">
              Your outfit
            </h2>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          {clothing.map((item) => (
            <OutfitCard
              key={`${item.category}-${item.name}`}
              category={item.category}
              name={item.name}
              image={item.imageUrl ?? undefined}
            />
          ))}
        </div>
      </section>

      {/* PACK IN BAG */}
      {packInBag.length > 0 && (
        <section>
          <div className="mb-3">
            <p className="text-[10px] font-bold uppercase tracking-[0.12em] text-[#E77D72]">
              Just in case
            </p>
            <h2 className="mt-1 font-serif text-[22px] leading-tight text-[#17294D]">
              Pack in your bag
            </h2>
            <p className="mt-1 text-xs leading-relaxed text-[#6F7480]">
              Useful extra layers or items to take with you.
            </p>
          </div>

          <div className="space-y-3">
            {packInBag.map((item) => (
              <PackInBagCard
                key={`${item.category}-${item.name}`}
                item={item}
              />
            ))}
          </div>
        </section>
      )}
    </div>
  );
}

function PackInBagCard({ item }: { item: ClothingItem }) {
  return (
    <article className="flex items-center gap-3 rounded-2xl border border-[#E6E8EC] bg-white p-2.5 shadow-[0_3px_12px_rgba(23,41,77,0.035)]">
      {/* Smaller image */}
      <div className="flex h-[72px] w-[72px] shrink-0 items-center justify-center overflow-hidden rounded-xl bg-[#F7F6F2]">
        {item.imageUrl ? (
          <img
            src={item.imageUrl}
            alt={item.name}
            className="h-full w-full object-contain mix-blend-multiply"
          />
        ) : (
          <div className="h-full w-full bg-[#F0EFEB]" />
        )}
      </div>

      {/* Item information */}
      <div className="min-w-0 flex-1">
        <p className="text-[9px] font-bold uppercase tracking-wider text-[#8A94A6]">
          {item.category}
        </p>

        <h3 className="mt-0.5 truncate text-[13px] font-bold text-[#17294D]">
          {item.name}
        </h3>

        <p className="mt-1 text-[10px] text-[#8A94A6]">Recommended to pack</p>
      </div>

      {/* Pack button */}
      <button
        type="button"
        className="shrink-0 rounded-xl border border-[#E77D72] px-3 py-2 text-[11px] font-bold text-[#E77D72] transition-colors hover:bg-[#FFF3F1] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#E77D72] focus-visible:ring-offset-2"
      >
        Pack in bag
      </button>
    </article>
  );
}
