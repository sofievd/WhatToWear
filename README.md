# WearWise

Know what to wear based on the weather.

### Authentication

The app supports both guest and authenticated use. Guests can use weather and outfit features without an account; their preferences stay in the browser. Clerk accounts persist preferences in the backend database.

To enable authentication locally:

1. Create a Clerk application and copy its publishable key into `frontend/.env` as `VITE_CLERK_PUBLISHABLE_KEY`.
2. Set the matching Clerk issuer URL in the backend environment as `CLERK_ISSUER_URL`.
3. Start the frontend and backend normally.

Without these variables, the frontend remains available in guest mode. The authenticated preferences API is `GET` and `POST /api/preferences`.

## Backend

### Getting started

Follow the steps below to set up and run the application locally.

### 1. Clone the repository

Clone the repository and navigate to the project directory.

### 2. Navigate to the backend

Change to the backend directory:

```
cd backend
```

### 3. Start Docker

Start the required Docker services using Docker Compose:

```
docker compose up
```

### 4. Configure Liquibase

If you are running the application **without a database** for development, disable Liquibase in `applications.yaml`:

```
liquibase:  enabled: false
```

If you are using a database, leave Liquibase enabled:

```
liquibase:  enabled: true
```

### 5. Run the application

Start the application using the appropriate run configuration or command for your development environment.

1.
