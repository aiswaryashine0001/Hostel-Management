@echo off
echo 🔍 Testing Registration Form...

REM Set JAVA_HOME
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot"

echo.
echo 🚀 Starting application...
echo 📱 Open: http://localhost:8080/register
echo 🔧 Check browser console (F12) for JavaScript errors
echo 👤 Test with: student123, John Doe, john@test.com
echo.

mvnw.cmd spring-boot:run