import { OutfitCard } from "./OutfitCard";

const outfits = [
  {
    category: "BASE LAYER",
    name: "Moisture-Wicking Tee",
    image:
      "https://images.unsplash.com/photo-1581655353564-df123a1eb820?ixid=M3w4MjcwNjd8MHwxfHNlYXJjaHwxfHx0LXNoaXJ0fGVufDB8fHx8MTc4ODM0MTczN3ww&ixlib=rb-4.1.0&fit=max&q=80",
  },
  {
    category: "MID LAYER",
    name: "Light Fleece Jacket",
    image:
      "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?ixid=M3w4MjcwNjd8MHwxfHNlYXJjaHwxfHxqYWNrZXR8ZW58MHx8fHwxNzg4MzQxNzY4fDA&ixlib=rb-4.1.0&fit=max&q=80",
  },
  {
    category: "OUTER LAYER",
    name: "Windbreaker Shield",
    image:
      "https://images.unsplash.com/photo-1633965288332-cba23d7e157d?ixid=M3w4MjcwNjd8MHwxfHNlYXJjaHwxfHxXaW5kYnJlYWtlciUyMFNoaWVsZHxlbnwwfHx8fDE3ODgzNDE3OTN8MA&ixlib=rb-4.1.0&fit=max&q=80",
  },
  {
    category: "BOTTOM",
    name: "Athletic Joggers",
    image:
      "https://images.unsplash.com/photo-1480179087180-d9f0ec044897?ixid=M3w4MjcwNjd8MHwxfHNlYXJjaHwxfHxqb2dnZXJ8ZW58MHx8fHwxNzg4MzQxODI4fDA&ixlib=rb-4.1.0&fit=max&q=80",
  },
  {
    category: "FOOTWEAR",
    name: "Trail Runners",
    image:
      "https://images.unsplash.com/photo-1542291026-7eec264c27ff?ixid=M3w4MjcwNjd8MHwxfHNlYXJjaHwxfHxydW5uZXIlMjBzaG9lc3xlbnwwfHx8fDE3ODgzNDE4NDV8MA&ixlib=rb-4.1.0&fit=max&q=80",
  },
  {
    category: "ACCESSORIES",
    name: "Sunglasses & Cap",
    image:
      "https://images.unsplash.com/photo-1590564310418-66304f55a2c2?ixid=M3w4MjcwNjd8MHwxfHNlYXJjaHwxfHxzdW5nbGFzc2VzJTIwYW5kJTIwY2FwfGVufDB8fHx8MTc4ODM0MTg2OXww&ixlib=rb-4.1.0&fit=max&q=80",
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
          image={outfit.image}
        />
      ))}
    </div>
  );
}
