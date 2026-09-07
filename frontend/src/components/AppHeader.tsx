import WearWiseLogo from "../assets/logo.png";
import { AuthControls } from "./AuthControls";

export function AppHeader() {
  return (
    <header className="relative flex justify-center px-1 pb-3 pt-3">
      <img
        src={WearWiseLogo}
        alt="WearWise"
        className="h-35 w-auto object-contain"
      />
      <AuthControls />
    </header>
  );
}
