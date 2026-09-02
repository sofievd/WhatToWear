interface OutfitCardProps {
  category: string;
  name: string;
  image?: string;
}

export function OutfitCard({ category, name, image }: OutfitCardProps) {
  return (
    <article className="flex flex-col overflow-hidden rounded-[20px] bg-white shadow-[0_4px_16px_rgba(23,41,77,0.04)]">
      {/* 
        1. THE CONTAINER: We give it a strict height (140px).
        2. relative: This traps the absolute image inside this specific box. 
      */}
      <div className="relative w-full h-[160px] overflow-hidden">
        {image ? (
          <img
            src={image}
            alt={name}
         
            className="absolute inset-0 w-full h-full object-contain scale-125 mix-blend-multiply"
          />
        ) : (
          <div className="absolute inset-0 m-4 rounded-md bg-[#F0EFEB]" />
        )}
      </div>

      {/* Text Container */}
      <div className="flex flex-col px-4 pb-4 pt-1">
        <p className="text-[10px] font-bold uppercase tracking-wider text-[#8A94A6]">
          {category}
        </p>

        <h3 className="mt-1 text-[13px] font-bold leading-snug text-[#17294D]">
          {name}
        </h3>
      </div>
    </article>
  );
}
