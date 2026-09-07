import type { CitySuggestion } from "../types/types";
import { apiClient } from "./client";

export function getCitySuggestions(query: string){
  return apiClient<CitySuggestion[]>(
    `/api/cities?query=${encodeURIComponent(query)}`,
  ); 
}