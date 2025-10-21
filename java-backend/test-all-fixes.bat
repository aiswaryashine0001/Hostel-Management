@echo off
echo 🔄 Restarting with all CSS fixes...

REM Set environment
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot"

echo ✅ Fixed Issues:
echo    1. Dashboard CSS - Now has proper Bootstrap styling
echo    2. Admin Dashboard CSS - Complete HTML structure  
echo    3. Registration form - Will clear inputs after success
echo    4. All pages - Proper Thymeleaf syntax
echo.

echo 🚀 Starting application...
echo.
echo 📱 Test URLs:
echo    Home: http://localhost:8080
echo    Register: http://localhost:8080/register  
echo    Login: http://localhost:8080/login
echo    Admin: http://localhost:8080/admin (admin/admin123)
echo.

mvnw.cmd spring-boot:run