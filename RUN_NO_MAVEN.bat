@echo off
REM =====================================================
REM  NO MAVEN REQUIRED - Using Maven Wrapper
REM =====================================================

echo.
echo ==========================================
echo  HOSTEL MANAGEMENT - NO DOWNLOADS NEEDED
echo ==========================================
echo.

REM Check current directory
if not exist "java-backend" (
    echo ❌ Please run this from the Hostel-Management main directory
    echo    Expected: Hostel-Management\java-backend\
    echo    Current: %CD%
    pause
    exit /b 1
)

echo ✅ Found java-backend directory
echo.

REM Step 1: Check Java only
echo [1/3] Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is NOT installed
    echo.
    echo 📥 Please install Java 17+ first:
    echo    1. Go to: https://adoptium.net/temurin/releases/
    echo    2. Download OpenJDK 17 LTS for Windows x64 (.msi)
    echo    3. Install and restart this script
    echo.
    pause
    start https://adoptium.net/temurin/releases/
    exit /b 1
) else (
    echo ✅ Java is installed
    java -version
    echo.
)

REM Step 2: Navigate to project
echo [2/3] Navigating to project...
cd java-backend
echo ✅ Ready to use Maven Wrapper (no manual Maven installation needed!)
echo.

REM Step 3: Run using Maven Wrapper
echo [3/3] Starting application using Maven Wrapper...
echo.
echo 📦 Maven Wrapper will automatically download Maven if needed
echo 🚀 Starting Hostel Management System...
echo.
echo ┌─────────────────────────────────────────┐
echo │  HOSTEL MANAGEMENT SYSTEM               │
echo │                                         │
echo │  🌐 URL: http://localhost:8080          │
echo │  👤 Admin: admin / admin123             │
echo │  🗃️  Database: http://localhost:8080/h2-console │
echo │                                         │
echo │  ⏹️  Press Ctrl+C to stop              │
echo └─────────────────────────────────────────┘
echo.

REM Use Maven Wrapper - no Maven installation required!
mvnw.cmd spring-boot:run

echo.
echo 🛑 Application stopped
pause