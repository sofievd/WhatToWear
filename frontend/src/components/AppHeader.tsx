import WearWiseLogo from "../assets/logo.png";

export function AppHeader() {
  return (
    <header className="flex justify-center pt-8 pb-6">
      <img
        src={WearWiseLogo}
        alt="WearWise"
        className="h-14 w-auto object-contain"
      />
    </header>
  );
}
