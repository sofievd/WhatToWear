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
    return null;
  }

  if (isLoading) {
    return (
      <div className="rounded-[28px] border border-[#E6E8EC] bg-white p-4 shadow-[0_6px_24px_rgba(23,41,77,0.04)]">
        <div className="mb-4 space-y-2">
          <div className="h-3 w-20 animate-pulse rounded-full bg-[#F0EFEB]" />
          <div className="h-7 w-36 animate-pulse rounded-lg bg-[#F0EFEB]" />
        </div>

        <div className="grid grid-cols-2 gap-3">
          {[0, 1, 2, 3].map((item) => (
            <div
              key={item}
              className="h-52 animate-pulse rounded-[20px] bg-[#F0EFEB]"
            />
          ))}
        </div>
      </div>
    );
  }

  if (isError) {
    return (
      <div className="rounded-[28px] border border-[#F0C2BD] bg-[#FFF5F3] p-5">
        <p className="text-sm font-medium text-[#A34B43]">
          We could not load your outfit right now.
        </p>

        <p className="mt-1 text-xs leading-relaxed text-[#B56A63]">
          Please try again in a moment.
        </p>
      </div>
    );
  }

  if (clothing.length === 0) {
    return (
      <div className="rounded-[28px] border border-[#E6E8EC] bg-white p-5">
        <p className="text-sm text-[#667085]">
          No outfit is available for this weather yet.
        </p>
      </div>
    );
  }

  return (
    <div className="overflow-hidden rounded-[28px] border border-[#E6E8EC] bg-white shadow-[0_6px_24px_rgba(23,41,77,0.04)]">
      {/* HEADER */}
      <div className="px-4 pb-3 pt-5">
        <p className="text-[10px] font-bold uppercase tracking-[0.14em] text-[#E77D72]">
          Wear now
        </p>

        <div className="mt-1 flex items-end justify-between">
          <h2 className="font-serif text-[25px] leading-tight text-[#17294D]">
            Your outfit
          </h2>

          <span className="pb-0.5 text-[11px] text-[#8A94A6]">
            Based on today
          </span>
        </div>
      </div>

      {/* MAIN OUTFIT */}
      <div className="px-4 pb-5">
        <div className="grid grid-cols-2 gap-3">
          {clothing.map((item) => (
            <OutfitCard
              key={`${item.category}-${item.name}`}
              category={item.category}
              name={item.name}
              image={item.imageUrl ?? undefined}
            />
          ))}
        </div>
      </div>

      {/* PACK IN BAG */}
      {packInBag.length > 0 && (
        <>
          <div className="mx-4 border-t border-[#ECEDEF]" />

          <div className="px-4 pb-5 pt-5">
            <div className="mb-3 flex items-start justify-between">
              <div>
                <p className="text-[10px] font-bold uppercase tracking-[0.14em] text-[#8A94A6]">
                  Just in case
                </p>

                <h3 className="mt-1 font-serif text-[20px] leading-tight text-[#17294D]">
                  Pack in your bag
                </h3>
              </div>

              <span className="rounded-full bg-[#F7F6F2] px-2.5 py-1 text-[10px] font-medium text-[#7B8190]">
                {packInBag.length} {packInBag.length === 1 ? "item" : "items"}
              </span>
            </div>

            <div className="space-y-2">
              {packInBag.map((item) => (
                <PackInBagCard
                  key={`${item.category}-${item.name}`}
                  item={item}
                />
              ))}
            </div>
          </div>
        </>
      )}
    </div>
  );
}

function PackInBagCard({ item }: { item: ClothingItem }) {
  return (
    <article className="flex items-center gap-3 rounded-2xl bg-[#F8F7F4] p-2.5">
      {/* IMAGE */}
      <div className="flex h-[58px] w-[58px] shrink-0 items-center justify-center overflow-hidden rounded-xl bg-white">
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

      {/* INFO */}
      <div className="min-w-0 flex-1">
        <p className="text-[9px] font-bold uppercase tracking-[0.1em] text-[#9AA1AE]">
          {item.category}
        </p>

        <h4 className="mt-0.5 truncate text-[13px] font-bold text-[#17294D]">
          {item.name}
        </h4>

        <p className="mt-0.5 text-[10px] text-[#9298A4]">
          Handy to have with you
        </p>
      </div>
    </article>
  );
}
