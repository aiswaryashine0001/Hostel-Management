import sqlite3
import hashlib
import os

def create_database():
    """Create and initialize the hostel management database"""
    
    db_path = 'hostel_management.db'
    
    # Remove existing database if it exists
    if os.path.exists(db_path):
        os.remove(db_path)
    
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    
    # Students table
    cursor.execute('''
        CREATE TABLE students (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id TEXT UNIQUE NOT NULL,
            name TEXT NOT NULL,
            email TEXT UNIQUE NOT NULL,
            phone TEXT,
            password_hash TEXT NOT NULL,
            course TEXT,
            year INTEGER,
            gender TEXT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
    ''')
    
    # Rooms table
    cursor.execute('''
        CREATE TABLE rooms (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            room_number TEXT UNIQUE NOT NULL,
            capacity INTEGER DEFAULT 2,
            occupied INTEGER DEFAULT 0,
            floor INTEGER,
            building TEXT,
            amenities TEXT,
            status TEXT DEFAULT 'available'
        )
    ''')
    
    # Student preferences table
    cursor.execute('''
        CREATE TABLE student_preferences (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id INTEGER,
            sleep_time TEXT,
            wake_time TEXT,
            study_preference TEXT,
            noise_tolerance TEXT,
            cleanliness_level TEXT,
            social_preference TEXT,
            music_preference TEXT,
            visitor_frequency TEXT,
            temperature_preference TEXT,
            smoking_preference TEXT,
            dietary_preferences TEXT,
            interests TEXT,
            additional_notes TEXT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (student_id) REFERENCES students (id)
        )
    ''')
    
    # Room allocations table
    cursor.execute('''
        CREATE TABLE room_allocations (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            student_id INTEGER,
            room_id INTEGER,
            allocation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            status TEXT DEFAULT 'active',
            compatibility_score REAL,
            FOREIGN KEY (student_id) REFERENCES students (id),
            FOREIGN KEY (room_id) REFERENCES rooms (id)
        )
    ''')
    
    # Admin table
    cursor.execute('''
        CREATE TABLE admin (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE NOT NULL,
            password_hash TEXT NOT NULL,
            email TEXT
        )
    ''')
    
    # Insert default admin user (username: admin, password: admin123)
    admin_password = hashlib.sha256('admin123'.encode()).hexdigest()
    cursor.execute('''
        INSERT INTO admin (username, password_hash, email)
        VALUES (?, ?, ?)
    ''', ('admin', admin_password, 'admin@hostel.edu'))
    
    # Insert sample rooms
    sample_rooms = [
        ('A101', 2, 0, 1, 'Block A', 'WiFi, AC, Study Table'),
        ('A102', 2, 0, 1, 'Block A', 'WiFi, AC, Study Table'),
        ('A103', 2, 0, 1, 'Block A', 'WiFi, AC, Study Table'),
        ('A201', 2, 0, 2, 'Block A', 'WiFi, AC, Study Table'),
        ('A202', 2, 0, 2, 'Block A', 'WiFi, AC, Study Table'),
        ('B101', 3, 0, 1, 'Block B', 'WiFi, Fan, Study Table'),
        ('B102', 3, 0, 1, 'Block B', 'WiFi, Fan, Study Table'),
        ('B201', 3, 0, 2, 'Block B', 'WiFi, Fan, Study Table'),
        ('C101', 4, 0, 1, 'Block C', 'WiFi, AC, Study Table, Balcony'),
        ('C102', 4, 0, 1, 'Block C', 'WiFi, AC, Study Table, Balcony'),
    ]
    
    cursor.executemany('''
        INSERT INTO rooms (room_number, capacity, occupied, floor, building, amenities)
        VALUES (?, ?, ?, ?, ?, ?)
    ''', sample_rooms)
    
    conn.commit()
    conn.close()
    
    print("Database created successfully!")
    print("Default admin credentials - Username: admin, Password: admin123")

if __name__ == '__main__':
    create_database()