# Copilot Instructions for AI Coding Agents

## Project Overview
- This is a Vite + React 18 project for an e-commerce web app ("UniClub") with local mock data and client-side state management.
- Main features: product catalog, cart, checkout, user authentication, order management, and admin order view.
- No backend/API calls; all data is managed in-memory via `src/lib/mock-data.js` and browser `localStorage`.

## Architecture & Key Patterns
- **Routing:** Uses `react-router-dom` (see `src/App.jsx`) for page navigation. Route paths map to page components in `src/pages/`.
- **State:** Cart and auth state are stored in `localStorage` via utility functions in `src/lib/cart.js` and `src/lib/auth.js`.
- **Theme:** Light/dark theme is managed by `ThemeProvider` (`src/components/theme-provider.jsx`), with theme stored in `localStorage`.
- **UI:** Tailwind CSS is used for styling. Utility functions for class merging are in `src/lib/utils.js`.
- **Product Filtering:** Sidebar filters (see `SidebarFilter.jsx`) update URL search params for deep-linking and state restoration.
- **Mock Data:** All entities (products, users, orders, etc.) are defined in `src/lib/mock-data.js`.

## Developer Workflows
- **Start Dev Server:** `npm run dev` (runs on port 3000, see `vite.config.js`).
- **Build for Production:** `npm run build`
- **Preview Build:** `npm run preview`
- **No test scripts or test framework present.**

## Project-Specific Conventions
- **File Structure:**
  - `src/pages/` for route-level components
  - `src/components/` for shared UI and feature components
  - `src/lib/` for utilities, mock data, and state helpers
- **Data Flow:** All data is read from and written to local state or `localStorage`. No external API integration.
- **Vietnamese Locale:** Currency formatting and some UI text use Vietnamese conventions (see `formatPrice` in `src/lib/utils.js`).
- **Admin vs. Customer:** Role-based logic is handled in `auth.js` and mock data. Admin routes are prefixed with `/admin`.

## Integration Points
- **External Libraries:**
  - React 18, React Router v7, Vite, Tailwind CSS, Sonner (toast notifications)
  - No backend, no API, no database
- **Alias:** `@` resolves to `src/` (see `vite.config.js`)

## Examples & Patterns
- **Add to Cart:** Use `addToCart(item)` from `src/lib/cart.js`.
- **Authentication:** Use `login`, `register`, `logout`, `getCurrentUser` from `src/lib/auth.js`.
- **Theme Switch:** Use `useTheme()` hook from `theme-provider.jsx`.
- **Product Filtering:** Use `SidebarFilter` and update URL search params for filter state.

## Key Files
- `src/App.jsx` (routing)
- `src/lib/mock-data.js` (all mock entities)
- `src/lib/cart.js` (cart state)
- `src/lib/auth.js` (auth state)
- `src/components/theme-provider.jsx` (theme logic)
- `src/pages/Products.jsx` (filtering, product list)

---
**If any conventions or workflows are unclear, ask the user for clarification or examples.**
