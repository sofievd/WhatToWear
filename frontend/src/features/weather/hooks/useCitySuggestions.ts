import { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { getCitySuggestions } from "../api/cityApi";

export function useCitySuggestions(query: string) {
  const [debouncedQuery, setDebouncedQuery] = useState("");

  useEffect(() => {
    const timeoutId = window.setTimeout(() => {
      setDebouncedQuery(query.trim());
    }, 350);

    return () => window.clearTimeout(timeoutId);
  }, [query]);

  return useQuery({
    queryKey: ["city-suggestions", debouncedQuery],
    queryFn: () => getCitySuggestions(debouncedQuery),
    enabled: debouncedQuery.length >= 2,
  });
}
