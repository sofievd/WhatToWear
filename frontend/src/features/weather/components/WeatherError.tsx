interface WeatherErrorProps {
  message: string;
  onRetry?: () => void;
}
export function WeatherError({ message, onRetry }: WeatherErrorProps) {
  return (
    <section role="alert" aria-label="Weather data error">
      <p>{message}</p>
      {onRetry && (
        <button type="button" onClick={onRetry}>
          Try again
        </button>
      )}
    </section>
  );
}
