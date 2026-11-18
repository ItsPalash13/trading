# Broker App

Vite + React + Redux Toolkit (RTK Query) frontend for the Trading broker backend.

Auth/session/profile calls live in `src/store/api.ts`.

## Pages

- `/login` — SPA page; auth via `POST /api/auth/login`
- `/signup` — `POST /api/auth/signup`, then auto-login
- `/dashboard` — protected home
- `/profile` — `GET /api/v1/users/me`

## Run

Backend (port 8080) should already be running, then:

```bash
npm install
npm run dev
```

Dev server: http://localhost:5173  
API calls are proxied to http://localhost:8080.
