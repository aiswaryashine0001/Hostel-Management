@echo off
echo Testing Admin Dashboard - Student Loading Fix...
echo.
echo Changes made:
echo 1. Added /api/students endpoint to AdminController  
echo 2. Enhanced error handling in admin dashboard JavaScript
echo 3. Added console logging for debugging
echo.
echo To test:
echo 1. Navigate to: http://localhost:8080/admin/login
echo 2. Login with admin credentials
echo 3. Check browser console (F12) for detailed error messages
echo 4. Students should now load properly in the dashboard
echo.
cd /d "d:\Hostel-Management"
mvn spring-boot:run
pause