@echo off
echo ===============================
echo  RUNNING FIXED APPLICATION
echo ===============================
echo.

REM Set JAVA_HOME (adjust path if different)
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot"

echo Starting Spring Boot application...
echo.
echo ✅ Application will be available at: http://localhost:8080
echo ✅ Admin login: admin / admin123
echo.
echo Press Ctrl+C to stop the application
echo.

REM Run with Maven wrapper
mvnw.cmd spring-boot:run