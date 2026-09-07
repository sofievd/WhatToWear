const API_URL = import.meta.env.VITE_API_URL;

export async function apiClient<T>(
  endpoint: string,
  options?: RequestInit,
): Promise<T> {
  const url = `${API_URL}${endpoint}`;
  const response = await fetch(url, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...options?.headers,
    },
  });
  if (!response.ok){
    throw new Error(`API request failed: ${response.status}`);
  }
  if (response.status === 204) {
    return undefined as T;
  }
  return response.json() as Promise<T>;
}
