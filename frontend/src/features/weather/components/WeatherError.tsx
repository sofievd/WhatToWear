interface WeatherErrorProps {
  message: string;
  onRetry?: () => void;
}
export function WeatherError({ message, onRetry }: WeatherErrorProps) {
  return (
    <section
      role="alert"
      aria-label="Weather data error"
      className="flex flex-col items-center justify-center rounded-2xl border border-red-200 bg-red-50/80 p-6 text-center shadow-sm dark:border-red-900/50 dark:bg-red-950/30"
    >
      {" "}
      <div
        aria-hidden="true"
        className="mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-red-100 text-red-600 dark:bg-red-900/50 dark:text-red-400"
      >
        {" "}
        <svg
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          className="h-6 w-6"
        >
          {" "}
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M12 9v3.75m0 3.75h.008M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0Z"
          />{" "}
        </svg>{" "}
      </div>{" "}
      <h2 className="text-sm font-semibold text-red-900 dark:text-red-200">
        {" "}
        Unable to load weather{" "}
      </h2>{" "}
      <p className="mt-1 max-w-md text-sm leading-6 text-red-700 dark:text-red-300">
        {" "}
        {message}{" "}
      </p>{" "}
      {onRetry && (
        <button
          type="button"
          onClick={onRetry}
          className="mt-5 inline-flex items-center justify-center rounded-lg bg-red-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition-colors hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 active:bg-red-800 dark:focus:ring-offset-gray-950"
        >
          {" "}
          Try again{" "}
        </button>
      )}{" "}
    </section>
  );
}
