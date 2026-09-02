import WearWiseLogo from "../assets/logo.png";

export function AppHeader() {
  return (
    <header className="flex justify-center pt-3 pb-3">
      <img
        src={WearWiseLogo}
        alt="WearWise"
        className="h-35 w-auto object-contain"
      />
    </header>
  );
}
