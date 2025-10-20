#!/bin/bash

# Hostel Management System Setup Script
# This script sets up the complete hostel management system

echo "🏢 Hostel Management System Setup"
echo "================================="

# Check if Python is installed
if ! command -v python &> /dev/null; then
    echo "❌ Python is not installed. Please install Python 3.7+ first."
    exit 1
fi

# Check if pip is installed
if ! command -v pip &> /dev/null; then
    echo "❌ pip is not installed. Please install pip first."
    exit 1
fi

echo "✅ Python and pip found"

# Create virtual environment (optional but recommended)
echo "📦 Creating virtual environment..."
python -m venv hostel_env

# Activate virtual environment
echo "🔄 Activating virtual environment..."
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    source hostel_env/Scripts/activate
else
    source hostel_env/bin/activate
fi

# Install dependencies
echo "📥 Installing Python dependencies..."
pip install -r requirements.txt

# Initialize database
echo "🗄️ Initializing database..."
cd database
python init_db.py
cd ..

echo "✅ Setup completed successfully!"
echo ""
echo "🚀 To start the application:"
echo "   1. Navigate to the backend directory: cd backend"
echo "   2. Run the Flask app: python app.py"
echo "   3. Open your browser to: http://localhost:5000"
echo ""
echo "👤 Default admin credentials:"
echo "   Username: admin"
echo "   Password: admin123"
echo ""
echo "📖 For detailed instructions, see README.md"