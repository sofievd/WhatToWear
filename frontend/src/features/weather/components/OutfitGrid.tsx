import { useQuery } from "@tanstack/react-query";
import { useEffect } from "react";
import { getRecommendation } from "../api/weatherService";
import { OutfitCard } from "./OutfitCard";
import type {
  ClothingItem,
  OutfitRecommendation,
} from "../types/types";
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
    enabled:
      requestKey > 0 &&
      latitude !== null &&
      longitude !== null,
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
      ].filter(
        (item): item is ClothingItem => item !== null,
      )
    : [];

  if (requestKey === 0) {
    return (
      <p className="rounded-2xl border border-[#D9E1EE] bg-white p-4 text-sm text-[#667085]">
        Choose your city and preferences, then get a recommendation.
      </p>
    );
  }

  if (isLoading) {
    return (
      <div
        className="grid grid-cols-2 gap-4"
        aria-label="Loading clothing"
      >
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
  );
}