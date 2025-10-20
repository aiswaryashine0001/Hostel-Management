@echo off
REM Hostel Management System Setup Script for Windows
REM This script sets up the complete hostel management system

echo 🏢 Hostel Management System Setup
echo =================================

REM Check if Python is installed
python --version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Python is not installed. Please install Python 3.7+ first.
    pause
    exit /b 1
)

REM Check if pip is installed
pip --version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ pip is not installed. Please install pip first.
    pause
    exit /b 1
)

echo ✅ Python and pip found

REM Create virtual environment (optional but recommended)
echo 📦 Creating virtual environment...
python -m venv hostel_env

REM Activate virtual environment
echo 🔄 Activating virtual environment...
call hostel_env\Scripts\activate

REM Install dependencies
echo 📥 Installing Python dependencies...
pip install -r requirements.txt

REM Initialize database
echo 🗄️ Initializing database...
cd database
python init_db.py
cd ..

echo ✅ Setup completed successfully!
echo.
echo 🚀 To start the application:
echo    1. Navigate to the backend directory: cd backend
echo    2. Run the Flask app: python app.py
echo    3. Open your browser to: http://localhost:5000
echo.
echo 👤 Default admin credentials:
echo    Username: admin
echo    Password: admin123
echo.
echo 📖 For detailed instructions, see README.md
pause