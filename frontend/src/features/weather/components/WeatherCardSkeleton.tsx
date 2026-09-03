export function WeatherCardSkeleton() {
  return (
    <section
      className="weather-display skeleton-display"
      aria-label="Loading weather data"
    >
      <div className="temp-main">
        <div className="skeleton skeleton-temp" />
        <div className="skeleton skeleton-feels" />
      </div>

      <div className="weather-details">
        <div className="detail-item">
          <div className="skeleton skeleton-label" />
          <div className="skeleton skeleton-value" />
        </div>
        <div className="detail-item">
          <div className="skeleton skeleton-label" />
          <div className="skeleton skeleton-value" />
        </div>
        <div className="detail-item">
          <div className="skeleton skeleton-label" />
          <div className="skeleton skeleton-value" />
        </div>
      </div>
    </section>
  );
}
