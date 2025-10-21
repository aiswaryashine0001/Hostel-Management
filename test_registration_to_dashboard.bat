@echo off
echo ========================================
echo TESTING STUDENT REGISTRATION TO DASHBOARD
echo ========================================
echo.
echo This will test the complete flow:
echo 1. Start the application
echo 2. Register a test student
echo 3. Check if student appears in admin dashboard
echo.
echo Steps to test manually:
echo.
echo 1. Wait for application to start
echo 2. Open: http://localhost:8080/register
echo 3. Register a student with these details:
echo    - Student ID: TEST001
echo    - Name: Test Student
echo    - Email: test@example.com
echo    - Phone: 1234567890
echo    - Password: test123
echo    - Course: Computer Science
echo    - Year: 2
echo    - Gender: Male
echo.
echo 4. Open: http://localhost:8080/admin/login
echo 5. Login with: admin / admin123
echo 6. Check if TEST001 appears in the dashboard
echo.
echo Starting application...
echo ========================================
cd /d "d:\Hostel-Management\java-backend"
mvn spring-boot:run
pause