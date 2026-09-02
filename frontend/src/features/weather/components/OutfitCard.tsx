interface OutfitCardProps {
  category: string;
  name: string;
  image?: string;
}

export function OutfitCard({
  category,
  name,
  image,
}: OutfitCardProps) {
  return (
    <article className="overflow-hidden rounded-3xl bg-white shadow-sm">
      <div className="flex h-48 items-center justify-center bg-white">
        {image ? (
          <img
            src={image}
            alt={name}
            className="h-full w-full object-contain p-5"
          />
        ) : (
          <div className="h-full w-full bg-slate-50" />
        )}
      </div>

      <div className="px-4 pb-5 pt-3">
        <p className="text-xs font-bold tracking-wide text-slate-500">
          {category}
        </p>

        <h3 className="mt-1 text-base font-bold leading-tight text-slate-900">
          {name}
        </h3>
      </div>
    </article>
  );
}