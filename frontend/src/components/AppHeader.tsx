import WearWiseLogo from "../assets/logo.png";
import { AuthControls } from "./AuthControls";

export function AppHeader() {
  return (
    <header className="flex h-14 items-center justify-between px-1">
      <img
        src={WearWiseLogo}
        alt="WearWise"
        className="h-16 w-auto object-contain"
      />

      <div className="flex items-center">
        <AuthControls />
      </div>
    </header>
  );
}