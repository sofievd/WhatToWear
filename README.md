# WearWise

weather prediction app 

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
