import { useEffect, useState } from "react";
import {
  Activity,
  ChevronDown,
  SlidersHorizontal,
  Sparkles,
  Thermometer,
  X,
} from "lucide-react";

export type Preferences = {
  activity: "sitting" | "walking" | "biking" | "jogging" | "running";
  thermalSensitivity: "cold" | "neutral" | "hot";
  style: "neutral" | "feminine" | "masculine";
};

type PreferencesBarProps = {
  onSave?: (preferences: Preferences) => void;
};

const defaultPreferences: Preferences = {
  activity: "walking",
  thermalSensitivity: "neutral",
  style: "neutral",
};

const activityOptions: Array<{
  label: string;
  value: Preferences["activity"];
}> = [
  { label: "Sitting", value: "sitting" },
  { label: "Walking", value: "walking" },
  { label: "Biking", value: "biking" },
  { label: "Jogging", value: "jogging" },
  { label: "Running", value: "running" },
];

const thermalOptions: Array<{
  label: string;
  value: Preferences["thermalSensitivity"];
}> = [
  { label: "Runs Cold", value: "cold" },
  { label: "Neutral", value: "neutral" },
  { label: "Runs Hot", value: "hot" },
];

const styleOptions: Array<{ label: string; value: Preferences["style"] }> = [
  { label: "Gender Neutral", value: "neutral" },
  { label: "More Feminine", value: "feminine" },
  { label: "More Masculine", value: "masculine" },
];

export function PreferencesBar({ onSave }: PreferencesBarProps) {
  const [isOpen, setIsOpen] = useState(false);
  const [preferences, setPreferences] = useState(defaultPreferences);

  useEffect(() => {
    if (!isOpen) {
      return;
    }

    function handleKeyDown(event: KeyboardEvent) {
      if (event.key === "Escape") {
        setIsOpen(false);
      }
    }

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [isOpen]);

  function updatePreference<Key extends keyof Preferences>(
    key: Key,
    value: Preferences[Key],
  ) {
    setPreferences((current) => ({ ...current, [key]: value }));
  }

  function handleSave() {
    onSave?.(preferences);
    setIsOpen(false);
  }

  return (
    <section className="space-y-3">
      <button
        type="button"
        aria-expanded={isOpen}
        aria-controls="preferences-panel"
        onClick={() => setIsOpen((current) => !current)}
        className="flex min-h-14 w-full items-center justify-between rounded-3xl border border-[#D9E1EE] bg-white px-5 text-left shadow-sm transition-shadow hover:shadow-md focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#6B91BB] focus-visible:ring-offset-2"
      >
        <span className="flex items-center gap-3">
          <SlidersHorizontal size={19} className="text-[#5D9DFF]" />
          <span className="text-sm font-bold text-[#17294D]">
            Preferences & Sensitivity
          </span>
        </span>

        <span className="flex items-center gap-3">
          <span className="text-sm text-[#3F90FF]">
            {preferences.activity[0].toUpperCase() + preferences.activity.slice(1)} · Default
          </span>
          <ChevronDown
            size={19}
            className={`text-slate-500 transition-transform ${isOpen ? "rotate-180" : ""}`}
          />
        </span>
      </button>

      {isOpen && (
        <div
          id="preferences-panel"
          className="space-y-3 rounded-[28px] border border-[#D9E1EE] bg-[#F5F7FC] p-4 shadow-sm"
        >
          <div className="flex items-start justify-between gap-3 px-1 pb-1">
            <div>
              <p className="text-[11px] font-bold uppercase tracking-[0.08em] text-[#E77D72]">
                Customization
              </p>
              <p className="mt-1 text-sm leading-5 text-[#667085]">
                Fine-tune the way we build your outfit.
              </p>
            </div>
            <button
              type="button"
              aria-label="Close preferences"
              onClick={() => setIsOpen(false)}
              className="rounded-full p-2 text-[#667085] transition-colors hover:bg-white hover:text-[#17294D] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#6B91BB]"
            >
              <X size={18} />
            </button>
          </div>

          <PreferenceGroup icon={<Activity size={19} />} title="Activity level">
            <div className="flex flex-wrap gap-2">
              {activityOptions.map((option) => (
                <PreferenceOption
                  key={option.value}
                  label={option.label}
                  selected={preferences.activity === option.value}
                  onClick={() => updatePreference("activity", option.value)}
                />
              ))}
            </div>
          </PreferenceGroup>

          <PreferenceGroup icon={<Thermometer size={19} />} title="Thermal sensitivity">
            <div className="grid grid-cols-3 gap-1 rounded-2xl bg-[#EDF1F9] p-1">
              {thermalOptions.map((option) => (
                <PreferenceOption
                  key={option.value}
                  label={option.label}
                  selected={preferences.thermalSensitivity === option.value}
                  onClick={() => updatePreference("thermalSensitivity", option.value)}
                  className="min-h-11 rounded-xl border-0 px-2 text-xs sm:text-sm"
                />
              ))}
            </div>
          </PreferenceGroup>

          <PreferenceGroup icon={<Sparkles size={19} />} title="Style preference">
            <div className="flex flex-wrap gap-2">
              {styleOptions.map((option) => (
                <PreferenceOption
                  key={option.value}
                  label={option.label}
                  selected={preferences.style === option.value}
                  onClick={() => updatePreference("style", option.value)}
                />
              ))}
            </div>
          </PreferenceGroup>

          <button
            type="button"
            onClick={handleSave}
            className="flex min-h-12 w-full items-center justify-center gap-2 rounded-2xl bg-[#6B91BB] px-4 text-sm font-bold text-white shadow-[0_8px_18px_rgba(107,145,187,0.2)] transition-colors hover:bg-[#5D83AC] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#6B91BB] focus-visible:ring-offset-2"
          >
            Save preferences
          </button>
        </div>
      )}
    </section>
  );
}

function PreferenceGroup({
  icon,
  title,
  children,
}: {
  icon: React.ReactNode;
  title: string;
  children: React.ReactNode;
}) {
  return (
    <section className="rounded-2xl border border-[#D9E1EE] bg-white p-4">
      <h2 className="mb-3 flex items-center gap-2 text-sm font-bold text-[#17294D]">
        <span className="text-[#6B91BB]">{icon}</span>
        {title}
      </h2>
      {children}
    </section>
  );
}

function PreferenceOption({
  label,
  selected,
  onClick,
  className = "",
}: {
  label: string;
  selected: boolean;
  onClick: () => void;
  className?: string;
}) {
  return (
    <button
      type="button"
      aria-pressed={selected}
      onClick={onClick}
      className={`min-h-11 rounded-full border px-4 text-sm font-semibold transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#6B91BB] ${
        selected
          ? "border-[#6B91BB] bg-[#6B91BB] text-white"
          : "border-[#D9E1EE] bg-white text-[#17294D] hover:border-[#9BB5D1]"
      } ${className}`}
    >
      {label}
    </button>
  );
}
