@echo off
echo Testing Database Connection...
echo.
echo Starting Hostel Management System...
echo Once started, you can test:
echo.
echo 1. Database Health Check: http://localhost:8080/api/health
echo 2. H2 Database Console: http://localhost:8080/h2-console
echo    - JDBC URL: jdbc:h2:file:./data/hostel_management
echo    - User: sa
echo    - Password: (leave empty)
echo.
echo 3. Admin Dashboard: http://localhost:8080/admin/login (admin/admin123)
echo 4. Student Registration: http://localhost:8080/register
echo.
cd /d "d:\Hostel-Management\java-backend"
mvn spring-boot:run
pause