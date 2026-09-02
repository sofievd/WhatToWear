import { OutfitCard } from "./OutfitCard";

const outfits = [
  {
    category: "BASE LAYER",
    name: "Moisture-Wicking Tee",
  },
  {
    category: "MID LAYER",
    name: "Light Fleece Jacket",
  },
  {
    category: "OUTER LAYER",
    name: "Windbreaker Shield",
  },
  {
    category: "BOTTOM",
    name: "Athletic Joggers",
  },
  {
    category: "FOOTWEAR",
    name: "Trail Runners",
  },
  {
    category: "ACCESSORIES",
    name: "Sunglasses & Cap",
  },
];

export function OutfitGrid() {
  return (
    <div className="grid grid-cols-2 gap-4">
      {outfits.map((outfit) => (
        <OutfitCard
          key={outfit.category}
          category={outfit.category}
          name={outfit.name}
        />
      ))}
    </div>
  );
}