import type { Config } from 'tailwindcss'

const config: Config = {
  // This tells Tailwind exactly where to look for your classes!
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}

export default config