import { useQuery } from "@tanstack/react-query";
import { getClothing } from "../api/weatherService";
import { OutfitCard } from "./OutfitCard";

export function OutfitGrid() {
  const { data: clothing = [], isLoading, isError } = useQuery({
    queryKey: ["clothing"],
    queryFn: getClothing,
  });

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
