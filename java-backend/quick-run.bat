@echo off
REM Quick Development Runner - No Maven Required
REM This script attempts to run the application using only Java classpath

echo =======================================
echo  Quick Java Runner (Development Only)
echo =======================================
echo.

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed
    echo Please install Java 17+ from: https://adoptium.net/
    pause
    exit /b 1
)

echo ℹ️  This is a development-only runner
echo ℹ️  For production, please use Maven: mvn spring-boot:run
echo.

REM Create a simple classpath (this is a simplified approach)
set CLASSPATH=src\main\java

REM Attempt to compile and run (basic approach)
echo 🔨 Attempting to compile Java sources...
dir /s /b src\main\java\*.java > sources.txt
javac -d classes -cp "%CLASSPATH%" @sources.txt 2>compile_errors.txt

if %errorlevel% neq 0 (
    echo ❌ Compilation failed. Errors:
    type compile_errors.txt
    echo.
    echo 💡 Recommendation: Install Maven and use proper build process
    echo    1. Install Maven from: https://maven.apache.org/
    echo    2. Run: mvn spring-boot:run
    pause
    del sources.txt compile_errors.txt 2>nul
    exit /b 1
)

echo ✅ Basic compilation successful
echo ⚠️  Note: Spring Boot dependencies are not loaded
echo ⚠️  Please install Maven for full functionality
echo.
echo 🔗 Installation Guide:
echo    Java: https://adoptium.net/temurin/releases/
echo    Maven: https://maven.apache.org/download.cgi
echo.

REM Cleanup
del sources.txt compile_errors.txt 2>nul
if exist classes (
    rmdir /s /q classes
)

echo 📖 Next Steps:
echo 1. Install Java 17+ and Maven
echo 2. Run setup.bat to verify installation
echo 3. Run run-application.bat to start the server
echo 4. Open http://localhost:8080 in your browser
echo.
pause