@echo off
REM Hostel Management System - Java Spring Boot Setup Script
REM This script helps set up the development environment for the Java version

echo =========================================
echo  Hostel Management System - Java Setup
echo =========================================
echo.

REM Check if Java is installed
echo [1/4] Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo.
    echo Please install Java 17 or higher:
    echo 1. Download from: https://adoptium.net/temurin/releases/
    echo 2. Install Java 17 LTS
    echo 3. Add Java to your PATH environment variable
    echo 4. Run this script again
    echo.
    goto :end
) else (
    echo ✅ Java is installed
    java -version
    echo.
)

REM Check if Maven is installed
echo [2/4] Checking Maven installation...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed or not in PATH
    echo.
    echo Please install Apache Maven:
    echo 1. Download from: https://maven.apache.org/download.cgi
    echo 2. Extract to a folder (e.g., C:\Program Files\Apache\maven)
    echo 3. Add Maven bin folder to your PATH environment variable
    echo 4. Run this script again
    echo.
    goto :end
) else (
    echo ✅ Maven is installed
    mvn -version
    echo.
)

REM Check project structure
echo [3/4] Checking project structure...
if not exist "pom.xml" (
    echo ❌ pom.xml not found. Make sure you're in the java-backend directory
    goto :end
)
if not exist "src\main\java" (
    echo ❌ Source directory not found. Project structure may be incomplete
    goto :end
)
echo ✅ Project structure looks good
echo.

REM Compile the project
echo [4/4] Compiling the project...
echo Running: mvn clean compile
mvn clean compile
if %errorlevel% neq 0 (
    echo ❌ Compilation failed. Please check the errors above.
    goto :end
)

echo.
echo ✅ Setup completed successfully!
echo.
echo Next steps:
echo 1. Run: mvn spring-boot:run
echo 2. Open browser to: http://localhost:8080
echo 3. Use admin credentials: admin / admin123
echo.
echo Alternatively, run: run-application.bat

:end
pause