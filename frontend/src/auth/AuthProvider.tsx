import { AuthContext, type AppAuth } from "./AuthContext";

export function AuthProvider({
  value,
  children,
}: {
  value: AppAuth;
  children: React.ReactNode;
}) {
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
