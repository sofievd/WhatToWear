import { useCallback, useEffect, useState } from "react";
import type { WeatherData } from "../types/types";
import { getWeatherData } from "../api/weatherService";

interface WeatherState {
  data: WeatherData | null;
  isLoading: boolean;
  error: string | null;
}

export function useWeather() {
  const [state, setState] = useState<WeatherState>({
    data: null,
    isLoading: true,
    error: null,
  });

  const loadWeather = useCallback(async () => {
    setState({
      data: null,
      isLoading: true,
      error: null,
    });

    try {
      const data = await getWeatherData();

      setState({
        data,
        isLoading: false,
        error: null,
      });
    } catch {
      setState({
        data: null,
        isLoading: false,
        error: "We couldn't load the weather right now.",
      });
    }
  }, []);

  useEffect(() => {
    void loadWeather();
  }, [loadWeather]);

  return {
    ...state,
    retry: loadWeather,
  };
}
