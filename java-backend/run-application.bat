@echo off
REM Hostel Management System - Application Runner
REM This script runs the Spring Boot application

echo =========================================
echo  Hostel Management System - Starting...
echo =========================================
echo.

REM Check if Java and Maven are available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please run setup.bat first.
    pause
    exit /b 1
)

mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed. Please run setup.bat first.
    pause
    exit /b 1
)

REM Check if project is compiled
if not exist "target\classes" (
    echo 🔨 Compiling project first...
    mvn clean compile
    if %errorlevel% neq 0 (
        echo ❌ Compilation failed
        pause
        exit /b 1
    )
)

echo 🚀 Starting Spring Boot application...
echo.
echo Application will be available at: http://localhost:8080
echo Admin credentials: admin / admin123
echo.
echo Press Ctrl+C to stop the application
echo.

REM Run the application
mvn spring-boot:run