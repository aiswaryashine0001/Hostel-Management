@echo off
echo 🔄 Restarting Hostel Management System...
echo.

REM Set JAVA_HOME if needed
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot

REM Clean and run
echo 🧹 Cleaning previous build...
call mvnw.cmd clean

echo 🔨 Compiling application...
call mvnw.cmd compile

echo 🚀 Starting application...
echo.
echo 📱 Open: http://localhost:8080
echo 👤 Admin: admin / admin123
echo.

call mvnw.cmd spring-boot:run