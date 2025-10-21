@echo off
echo Testing Hostel Management System - Logout Fix...
echo.
echo Changes made:
echo 1. Added GET /logout endpoint in StudentController
echo 2. Removed conflicting logout view controller from WebConfig
echo 3. Updated all templates to use /logout instead of /api/logout
echo.
echo Navigate to: http://localhost:8080
echo Login and then test logout functionality
echo.
cd /d "d:\Hostel-Management"
mvn spring-boot:run
pause