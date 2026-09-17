interface OutfitCardProps {
  category: string;
  name: string;
  image?: string;
}

export function OutfitCard({ category, name, image }: OutfitCardProps) {
  return (
    <article className="flex flex-col overflow-hidden rounded-[20px] bg-white shadow-[0_4px_16px_rgba(23,41,77,0.04)]">
      <div className="relative h-[155px] w-full overflow-hidden bg-[#FAF9F6]">
        {image ? (
          <img
            src={image}
            alt={name}
            className="absolute inset-0 h-full w-full object-contain mix-blend-multiply"
          />
        ) : (
          <div className="absolute inset-0 m-4 rounded-md bg-[#F0EFEB]" />
        )}
      </div>

      <div className="flex flex-col px-3.5 pb-3.5 pt-2">
        <p className="text-[9px] font-bold uppercase tracking-wider text-[#8A94A6]">
          {category}
        </p>

        <h3 className="mt-1 text-[13px] font-bold leading-snug text-[#17294D]">
          {name}
        </h3>
      </div>
    </article>
  );
}