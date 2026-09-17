# WearWise

WearWise is a weather-based outfit recommendation application. It uses weather information and user preferences to suggest suitable outfits for the current conditions.

## Features

- Weather information and predictions
- Outfit recommendations based on weather conditions
- Guest mode without requiring an account
- Optional authentication with Clerk
- User preferences stored locally for guests
- User preferences persisted in the backend for authenticated users

## Authentication

WearWise supports both **guest** and **authenticated** users.

Guests can use the weather and outfit recommendation features without creating an account. Their preferences are stored in the browser.

Authenticated users can sign in through Clerk, allowing their preferences to be stored and retrieved from the backend database.

### Enable authentication locally

1. Create a [Clerk](https://clerk.com/) application.
2. Add the Clerk publishable key to `frontend/.env`:
```env
VITE_CLERK_PUBLISHABLE_KEY=your_publishable_key
```

3. Configure the Clerk issuer URI in the backend environment:
```env
CLERK_JWT_ISSUER_URI=your_clerk_jwt_issuer_uri
```

4. Start the frontend and backend normally.

If the Clerk environment variables are not configured, the application can still be used in **guest mode**.

The authenticated preferences API provides:
```text
GET  /api/preferences
POST /api/preferences
```

## Backend

The backend is responsible for the application's API, weather-related functionality, authentication integration, and persistence of user preferences.

### Getting started

Follow the steps below to run the backend locally.

### 1. Clone the repository
```bash
git clone "https://github.com/sofievd/WhatToWear.git"
cd WearWise
```

### 2. Navigate to the backend
```bash
cd backend
```

### 3. Start Docker services

Start the required Docker services using Docker Compose:
```bash
docker compose up
```

### 4. Configure Liquibase

Liquibase is used to manage database changes.

If you are running the application **without a database** for development, disable Liquibase in `applications.yaml`:
```yaml
liquibase:
  enabled: false
```

If you are using a database, keep Liquibase enabled:
```yaml
liquibase:
  enabled: true
```

### 5. Configure environment variables

Make sure the required backend environment variables are configured before starting the application:

```env
CLERK_JWT_ISSUER_URI=your_clerk_jwt_issuer_uri
OPENROUTER_API_KEY=your_openrouter_api_key
```

### 6. Run the application

Start the backend using the appropriate run configuration or command for your development environment.

## Frontend

The frontend provides the user interface for weather information, outfit recommendations, and user preferences.

From the project root:
```bash
cd frontend
npm install
npm run dev
```

The frontend requires the following environment variables in `frontend/.env`:

```env
VITE_API_URL=your_backend_api_url
VITE_CLERK_PUBLISHABLE_KEY=your_publishable_key
```

Without the Clerk configuration, the frontend can still be used in guest mode.

## Project Structure
```text
WearWise/
├── backend/
├── frontend/
└── README.md
```

## Development

For local development, run the backend and frontend separately.

- **Backend:** API, authentication, database, and application logic
- **Frontend:** User interface and interaction with the backend API
- **Docker:** Supporting services required by the backend
- **Liquibase:** Database schema and migration management

## Notes

Authentication is optional. The application remains usable in guest mode when Clerk is not configured.

Do not commit `.env` files or API keys to the repository.
