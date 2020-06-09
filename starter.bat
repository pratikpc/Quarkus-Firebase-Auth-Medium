@set DATABASE_URL=postgresql://postgres:postgres@postgres:5432/notes
@set DEBUG_MODE=true
@set DB_CREATE=true
@set FIREBASE_PROJECT_ID=pc-firebase-auth-medium
@set PORT=8080
@mvnw compile quarkus:dev