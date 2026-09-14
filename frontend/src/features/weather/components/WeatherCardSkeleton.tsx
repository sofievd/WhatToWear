export function WeatherCardSkeleton() {
  return (
    <section
      className="rounded-2xl border border-[#E9E5DE] bg-white px-4 py-3 shadow-sm"
      aria-label="Loading weather data"
    >
      <div className="flex items-center justify-between gap-3">
        <div className="min-w-0 flex-1">
          <div className="flex items-center gap-2">
            <div className="h-5 w-24 animate-pulse rounded bg-[#E9E5DE]" />
            <div className="h-6 w-14 animate-pulse rounded bg-[#E9E5DE]" />
          </div>

          <div className="mt-2 h-3 w-48 animate-pulse rounded bg-[#F0ECE6]" />
        </div>

        <div className="h-10 w-10 shrink-0 animate-pulse rounded-full bg-[#F0ECE6]" />
      </div>

      <div className="mt-2 flex gap-4 border-t border-[#F0ECE6] pt-2">
        <div className="h-3 w-20 animate-pulse rounded bg-[#F0ECE6]" />
        <div className="h-3 w-20 animate-pulse rounded bg-[#F0ECE6]" />
      </div>
    </section>
  );
}
