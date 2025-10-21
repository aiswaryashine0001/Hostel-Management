@echo off
REM =======================================================
REM  HOSTEL MANAGEMENT SYSTEM - ONE-CLICK SETUP & RUN
REM =======================================================

echo.
echo ==========================================
echo  HOSTEL MANAGEMENT SYSTEM - FULL SETUP
echo ==========================================
echo.

REM Check current directory
if not exist "java-backend" (
    echo ❌ Please run this from the Hostel-Management main directory
    echo    Expected structure: Hostel-Management\java-backend\
    echo    Current location: %CD%
    echo.
    pause
    exit /b 1
)

echo ✅ Found java-backend directory
echo.

REM Step 1: Check Java
echo [1/6] Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is NOT installed
    echo.
    echo 📥 Please install Java 17+ first:
    echo    1. Go to: https://adoptium.net/temurin/releases/
    echo    2. Download OpenJDK 17 LTS for Windows x64 (.msi)
    echo    3. Install with default settings
    echo    4. Restart this script
    echo.
    echo 🔗 Direct link: https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.8.1%%2B1/OpenJDK17U-jdk_x64_windows_hotspot_17.0.8.1_1.msi
    echo.
    pause
    start https://adoptium.net/temurin/releases/
    exit /b 1
) else (
    echo ✅ Java is installed
    java -version
    echo.
)

REM Step 2: Check Maven
echo [2/6] Checking Maven installation...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is NOT installed
    echo.
    echo 📥 Please install Apache Maven:
    echo    1. Go to: https://maven.apache.org/download.cgi
    echo    2. Download apache-maven-3.9.x-bin.zip
    echo    3. Extract to C:\Program Files\Apache\
    echo    4. Add to PATH: C:\Program Files\Apache\apache-maven-3.9.x\bin
    echo    5. Restart this script
    echo.
    pause
    start https://maven.apache.org/download.cgi
    exit /b 1
) else (
    echo ✅ Maven is installed
    mvn -version | findstr "Apache Maven"
    echo.
)

REM Step 3: Navigate to project
echo [3/6] Navigating to project directory...
cd java-backend
if %errorlevel% neq 0 (
    echo ❌ Failed to navigate to java-backend directory
    pause
    exit /b 1
)
echo ✅ In project directory: %CD%
echo.

REM Step 4: Check project files
echo [4/6] Verifying project structure...
if not exist "pom.xml" (
    echo ❌ pom.xml not found - project may be incomplete
    pause
    exit /b 1
)
if not exist "src\main\java" (
    echo ❌ Source directory not found
    pause
    exit /b 1
)
echo ✅ Project structure verified
echo.

REM Step 5: Compile project
echo [5/6] Compiling the project...
echo Running: mvn clean compile
echo Please wait...
mvn clean compile
if %errorlevel% neq 0 (
    echo.
    echo ❌ Compilation FAILED
    echo    Check the error messages above
    echo    Common issues:
    echo    - Wrong Java version (need Java 17+)
    echo    - Missing dependencies
    echo    - Source code errors
    echo.
    pause
    exit /b 1
)
echo ✅ Compilation successful!
echo.

REM Step 6: Start application
echo [6/6] Starting the Hostel Management System...
echo.
echo 🚀 STARTING APPLICATION...
echo.
echo ┌─────────────────────────────────────────┐
echo │  HOSTEL MANAGEMENT SYSTEM               │
echo │                                         │
echo │  🌐 URL: http://localhost:8080          │
echo │  👤 Admin: admin / admin123             │
echo │  🗃️  Database: http://localhost:8080/h2-console │
echo │                                         │
echo │  📋 Features:                           │
echo │  ✅ Student Registration                │
echo │  ✅ Roommate Matching Algorithm         │
echo │  ✅ Admin Dashboard                     │
echo │  ✅ Room Management                     │
echo │                                         │
echo │  ⏹️  Press Ctrl+C to stop              │
echo └─────────────────────────────────────────┘
echo.
echo Starting server... Please wait for "Started HostelManagementApplication" message
echo.

REM Start the Spring Boot application
mvn spring-boot:run

echo.
echo 🛑 Application stopped
pause