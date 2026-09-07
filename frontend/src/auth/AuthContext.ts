import { createContext, useContext } from "react";

export type AppAuth = {
  isSignedIn: boolean;
  getToken: () => Promise<string | null>;
  clerkEnabled: boolean;
};

export const AuthContext = createContext<AppAuth>({
  isSignedIn: false,
  getToken: async () => null,
  clerkEnabled: false,
});

export function useAppAuth() {
  return useContext(AuthContext);
}
