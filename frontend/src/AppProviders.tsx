import { ClerkProvider, useAuth } from "@clerk/clerk-react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import App from "./app/App";
import { AuthProvider } from "./auth/AuthProvider";

const queryClient = new QueryClient();
const clerkPublishableKey = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY;

function AppWithQueryProvider() {
  return (
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  );
}

function GuestApp() {
  return (
    <AuthProvider
      value={{
        isSignedIn: false,
        getToken: async () => null,
        clerkEnabled: false,
      }}
    >
      <AppWithQueryProvider />
    </AuthProvider>
  );
}

function ClerkApp() {
  const { isSignedIn, getToken } = useAuth();

  return (
    <AuthProvider
      value={{
        isSignedIn: isSignedIn === true,
        getToken,
        clerkEnabled: true,
      }}
    >
      <AppWithQueryProvider />
    </AuthProvider>
  );
}

export function AppProviders() {
  if (!clerkPublishableKey) {
    return <GuestApp />;
  }

  return (
    <ClerkProvider publishableKey={clerkPublishableKey} afterSignOutUrl="/">
      <ClerkApp />
    </ClerkProvider>
  );
}
