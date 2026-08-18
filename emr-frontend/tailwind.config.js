/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        paper: "#F2F3EF",
        surface: "#FFFFFF",
        ink: "#17231F",
        "ink-soft": "#5B655F",
        "ink-faint": "#8B948E",
        border: "#DEE3DE",
        teal: {
          DEFAULT: "#0B5D52",
          dark: "#073D36",
          tint: "#E4EFEC",
        },
        gold: {
          DEFAULT: "#B4791F",
          tint: "#F6EBD8",
        },
        rust: {
          DEFAULT: "#A2402F",
          tint: "#F3E2DD",
        },
        slate: {
          DEFAULT: "#3E5266",
          tint: "#E4E9EE",
        },
      },
      fontFamily: {
        display: ["'Iowan Old Style'", "Georgia", "Cambria", "'Times New Roman'", "serif"],
        sans: [
          "-apple-system",
          "BlinkMacSystemFont",
          "'Segoe UI'",
          "Roboto",
          "Helvetica",
          "Arial",
          "sans-serif",
        ],
        mono: ["'SF Mono'", "ui-monospace", "'Courier New'", "monospace"],
      },
    },
  },
  plugins: [],
};
