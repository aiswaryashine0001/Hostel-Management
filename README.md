# Hostel Management System

A comprehensive hostel management system with intelligent roommate matching based on student preferences and compatibility scoring.

## Features

### 🎯 Core Features
- **Student Registration & Authentication**: Secure student registration and login system
- **Smart Roommate Matching**: Advanced algorithm matches students based on compatibility
- **Comprehensive Questionnaire**: Detailed preference form covering lifestyle, study habits, and personal preferences
- **Admin Dashboard**: Complete administrative control over students, rooms, and allocations
- **Room Management**: Create and manage hostel rooms with capacity and amenity tracking
- **Compatibility Scoring**: Intelligent scoring system for optimal roommate pairing

### 🧠 Matching Algorithm
The system uses a sophisticated compatibility algorithm that considers:
- **Sleep Patterns**: Sleep and wake time compatibility
- **Study Habits**: Group vs individual study preferences and noise tolerance
- **Personal Habits**: Cleanliness levels and lifestyle choices
- **Social Preferences**: Extrovert/introvert compatibility
- **Lifestyle Factors**: Music preferences, visitor frequency, temperature preferences
- **Critical Factors**: Smoking preferences (high weight in matching)
- **Shared Interests**: Common hobbies and interests bonus scoring

### 📊 Compatibility Scoring System
- **Time-based preferences**: Uses mathematical difference calculation for sleep/wake times
- **Ordinal preferences**: Compatibility scoring for low/medium/high preferences
- **Categorical matching**: Specific compatibility matrices for study and social preferences
- **Interest matching**: Jaccard similarity for shared interests
- **Weighted scoring**: Different preferences have different weights in final compatibility

## System Architecture

```
Hostel-Management/
├── backend/
│   ├── app.py                 # Main Flask application
│   └── room_allocation.py     # Room allocation algorithm
├── frontend/
│   ├── templates/             # HTML templates
│   │   ├── base.html         # Base template
│   │   ├── index.html        # Home page
│   │   ├── register.html     # Student registration
│   │   ├── login.html        # Student login
│   │   ├── preferences.html   # Preference questionnaire
│   │   ├── dashboard.html    # Student dashboard
│   │   ├── admin_login.html  # Admin login
│   │   └── admin_dashboard.html # Admin panel
│   └── static/
│       ├── css/
│       │   └── style.css     # Custom styles
│       └── js/
│           └── main.js       # JavaScript utilities
├── database/
│   └── init_db.py           # Database initialization
└── requirements.txt          # Python dependencies
```

## Installation & Setup

### Prerequisites
- Python 3.7 or higher
- pip (Python package installer)

### Step 1: Clone or Download
Download the project files to your local machine.

### Step 2: Install Dependencies
```bash
cd Hostel-Management
pip install -r requirements.txt
```

### Step 3: Initialize Database
```bash
cd database
python init_db.py
```
This creates the SQLite database with sample rooms and default admin credentials.

### Step 4: Run the Application
```bash
cd ../backend
python app.py
```

The application will start on `http://localhost:5000`

## Usage Guide

### For Students

1. **Registration**
   - Visit the homepage and click "Register Now"
   - Fill in personal details (Student ID, name, email, course, year, etc.)
   - Create a secure password

2. **Complete Preferences**
   - After registration, login and go to "Preferences"
   - Fill out the comprehensive questionnaire covering:
     - Sleep and wake times
     - Study preferences and noise tolerance
     - Cleanliness and social preferences
     - Music, temperature, and lifestyle preferences
     - Interests and hobbies
   - Save preferences to enter allocation pool

3. **View Dashboard**
   - Check registration and preference status
   - View room allocation once assigned
   - Access roommate information

### For Administrators

1. **Admin Login**
   - Default credentials: `admin` / `admin123`
   - Access admin dashboard for system management

2. **Manage Students**
   - View all registered students
   - Check preference completion status
   - Monitor allocation status

3. **Manage Rooms**
   - Add new rooms with capacity and amenities
   - View room occupancy status
   - Track available spaces

4. **Run Allocation**
   - Execute the smart allocation algorithm
   - View compatibility scores for assignments
   - Monitor allocation results and statistics

## Database Schema

### Students Table
- Personal information (ID, name, email, course, year, gender)
- Authentication (password hash)
- Registration timestamp

### Rooms Table
- Room details (number, building, floor, capacity)
- Occupancy tracking
- Amenities information

### Student Preferences Table
- Sleep and wake preferences
- Study habits and noise tolerance
- Personal and social preferences
- Lifestyle choices and interests

### Room Allocations Table
- Student-room assignments
- Compatibility scores
- Allocation timestamps and status

### Admin Table
- Administrator credentials and access control

## API Endpoints

### Authentication
- `POST /api/register` - Student registration
- `POST /api/login` - Student login
- `POST /api/admin_login` - Admin login

### Student Management
- `GET /api/students` - Get all students (admin only)
- `POST /api/preferences` - Save student preferences

### Room Management
- `GET /api/rooms` - Get all rooms
- `POST /api/create_room` - Create new room (admin only)

### Allocation System
- `POST /api/allocate_rooms` - Run room allocation algorithm (admin only)

## Configuration

### Security Settings
- Change the Flask secret key in `app.py`:
```python
app.secret_key = 'your-secure-secret-key-here'
```

### Database Configuration
- Default: SQLite database in `../database/hostel_management.db`
- Modify `DATABASE_PATH` in `app.py` to change location

### Compatibility Thresholds
- Minimum compatibility score for allocation: 60%
- Adjust weights in `room_allocation.py` for different preference priorities

## Customization

### Adding New Preference Categories
1. Update database schema in `init_db()` function
2. Add form fields in `preferences.html`
3. Update compatibility calculation in `room_allocation.py`
4. Modify weights and scoring logic as needed

### Modifying Compatibility Algorithm
- Adjust weights in `RoomAllocationSystem.calculate_compatibility_score()`
- Add new preference types in `_calculate_preference_compatibility()`
- Customize scoring functions for specific needs

### UI Customization
- Modify `frontend/static/css/style.css` for styling changes
- Update templates for layout modifications
- Add new JavaScript functionality in `main.js`

## Troubleshooting

### Common Issues

1. **Database Not Found**
   - Run `python database/init_db.py` to create the database

2. **Import Errors**
   - Install dependencies: `pip install -r requirements.txt`

3. **Template Not Found**
   - Ensure templates are in `frontend/templates/`
   - Check Flask template folder configuration

4. **Static Files Not Loading**
   - Verify static files are in `frontend/static/`
   - Check Flask static folder configuration

### Development Tips

1. **Enable Debug Mode**
   - Flask debug mode is enabled by default in `app.py`
   - Disable in production by setting `debug=False`

2. **Database Reset**
   - Delete `database/hostel_management.db`
   - Run `python database/init_db.py` to recreate

3. **Testing Allocation**
   - Register multiple students with different preferences
   - Complete preference questionnaires
   - Run allocation from admin dashboard

## Future Enhancements

### Potential Features
- **Email Notifications**: Automated emails for allocation updates
- **Mobile App**: React Native or Flutter mobile application
- **Advanced Analytics**: Dashboard with allocation statistics and trends
- **Feedback System**: Student feedback on roommate compatibility
- **Conflict Resolution**: System for handling roommate conflicts
- **Room Swapping**: Allow students to request room changes
- **Payment Integration**: Fee management and payment tracking
- **Visitor Management**: Track and manage visitor access
- **Maintenance Requests**: Report and track room maintenance issues

### Technical Improvements
- **API Documentation**: Swagger/OpenAPI documentation
- **Unit Tests**: Comprehensive test coverage
- **Performance Optimization**: Database indexing and query optimization
- **Security Enhancements**: JWT authentication, rate limiting
- **Deployment**: Docker containerization and cloud deployment guides
- **Monitoring**: Application performance monitoring and logging

## Contributing

To contribute to this project:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## Support

For support or questions:
- Create an issue in the project repository
- Review the troubleshooting section
- Check the API documentation

## License

This project is open source and available under the MIT License.

---

**Note**: This system is designed for educational purposes and small to medium-sized hostels. For large-scale deployments, consider additional security measures, database optimization, and scalability improvements.