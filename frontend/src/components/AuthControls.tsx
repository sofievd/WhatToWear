import {
  SignInButton,
  SignUpButton,
  SignedIn,
  SignedOut,
  UserButton,
} from "@clerk/clerk-react";
import { useAppAuth } from "../auth/AuthContext";

export function AuthControls() {
  const { clerkEnabled } = useAppAuth();

  if (!clerkEnabled) {
    return null;
  }

  return (
    <div className="absolute right-0 top-0 flex items-center gap-1.5">
      <SignedOut>
        <SignInButton mode="modal">
          <button
            type="button"
            className="rounded-full px-2 py-2 text-[11px] font-bold text-[#17294D] transition-colors hover:bg-[#EDF1F9] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#6B91BB]"
          >
            Log in
          </button>
        </SignInButton>
        <SignUpButton mode="modal">
          <button
            type="button"
            className="rounded-full bg-[#6B91BB] px-3 py-2 text-[11px] font-bold text-white transition-colors hover:bg-[#5D83AC] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#6B91BB]"
          >
            Sign up
          </button>
        </SignUpButton>
      </SignedOut>

      <SignedIn>
        <UserButton
          appearance={{
            elements: {
              userButtonAvatarBox: "h-9 w-9",
            },
          }}
        />
      </SignedIn>
    </div>
  );
}