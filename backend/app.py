from flask import Flask, render_template, request, jsonify, session, redirect, url_for
from flask_cors import CORS
import sqlite3
import hashlib
import os
from datetime import datetime
import json
from room_allocation import RoomAllocationSystem

app = Flask(__name__, template_folder='../frontend/templates', static_folder='../frontend/static')
app.secret_key = 'your-secret-key-change-in-production'
CORS(app)

# Database configuration
DATABASE_PATH = '../database/hostel_management.db'

def get_db_connection():
    """Get database connection"""
    conn = sqlite3.connect(DATABASE_PATH)
    conn.row_factory = sqlite3.Row
    return conn

def init_db():
    """Initialize database with all required tables"""
    conn = get_db_connection()
    
    # Students table
    conn.execute('''
        CREATE TABLE IF NOT EXISTS students (
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
    conn.execute('''
        CREATE TABLE IF NOT EXISTS rooms (
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
    conn.execute('''
        CREATE TABLE IF NOT EXISTS student_preferences (
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
    conn.execute('''
        CREATE TABLE IF NOT EXISTS room_allocations (
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
    conn.execute('''
        CREATE TABLE IF NOT EXISTS admin (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE NOT NULL,
            password_hash TEXT NOT NULL,
            email TEXT
        )
    ''')
    
    conn.commit()
    conn.close()

def hash_password(password):
    """Hash password using SHA256"""
    return hashlib.sha256(password.encode()).hexdigest()

def verify_password(password, hash):
    """Verify password against hash"""
    return hashlib.sha256(password.encode()).hexdigest() == hash

@app.route('/')
def index():
    """Home page"""
    return render_template('index.html')

@app.route('/register')
def register():
    """Student registration page"""
    return render_template('register.html')

@app.route('/login')
def login():
    """Login page"""
    return render_template('login.html')

@app.route('/admin')
def admin_login():
    """Admin login page"""
    return render_template('admin_login.html')

@app.route('/dashboard')
def dashboard():
    """Student dashboard"""
    if 'student_id' not in session:
        return redirect(url_for('login'))
    return render_template('dashboard.html')

@app.route('/admin_dashboard')
def admin_dashboard():
    """Admin dashboard"""
    if 'admin_id' not in session:
        return redirect(url_for('admin_login'))
    return render_template('admin_dashboard.html')

@app.route('/preferences')
def preferences():
    """Student preferences questionnaire"""
    if 'student_id' not in session:
        return redirect(url_for('login'))
    return render_template('preferences.html')

# API Routes

@app.route('/api/register', methods=['POST'])
def api_register():
    """Register a new student"""
    data = request.get_json()
    
    try:
        conn = get_db_connection()
        password_hash = hash_password(data['password'])
        
        conn.execute('''
            INSERT INTO students (student_id, name, email, phone, password_hash, course, year, gender)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ''', (data['student_id'], data['name'], data['email'], data['phone'], 
              password_hash, data['course'], data['year'], data['gender']))
        
        conn.commit()
        conn.close()
        
        return jsonify({'success': True, 'message': 'Registration successful'})
        
    except sqlite3.IntegrityError as e:
        return jsonify({'success': False, 'message': 'Student ID or email already exists'}), 400
    except Exception as e:
        return jsonify({'success': False, 'message': str(e)}), 500

@app.route('/api/login', methods=['POST'])
def api_login():
    """Student login"""
    data = request.get_json()
    
    conn = get_db_connection()
    student = conn.execute(
        'SELECT * FROM students WHERE student_id = ?', (data['student_id'],)
    ).fetchone()
    conn.close()
    
    if student and verify_password(data['password'], student['password_hash']):
        session['student_id'] = student['id']
        session['student_name'] = student['name']
        return jsonify({'success': True, 'message': 'Login successful'})
    else:
        return jsonify({'success': False, 'message': 'Invalid credentials'}), 401

@app.route('/api/admin_login', methods=['POST'])
def api_admin_login():
    """Admin login"""
    data = request.get_json()
    
    conn = get_db_connection()
    admin = conn.execute(
        'SELECT * FROM admin WHERE username = ?', (data['username'],)
    ).fetchone()
    conn.close()
    
    if admin and verify_password(data['password'], admin['password_hash']):
        session['admin_id'] = admin['id']
        session['admin_name'] = admin['username']
        return jsonify({'success': True, 'message': 'Admin login successful'})
    else:
        return jsonify({'success': False, 'message': 'Invalid admin credentials'}), 401

@app.route('/api/preferences', methods=['POST'])
def api_save_preferences():
    """Save student preferences"""
    if 'student_id' not in session:
        return jsonify({'success': False, 'message': 'Not authenticated'}), 401
    
    data = request.get_json()
    
    try:
        conn = get_db_connection()
        
        # Check if preferences already exist
        existing = conn.execute(
            'SELECT id FROM student_preferences WHERE student_id = ?', 
            (session['student_id'],)
        ).fetchone()
        
        if existing:
            # Update existing preferences
            conn.execute('''
                UPDATE student_preferences SET
                sleep_time = ?, wake_time = ?, study_preference = ?, noise_tolerance = ?,
                cleanliness_level = ?, social_preference = ?, music_preference = ?,
                visitor_frequency = ?, temperature_preference = ?, smoking_preference = ?,
                dietary_preferences = ?, interests = ?, additional_notes = ?
                WHERE student_id = ?
            ''', (data['sleep_time'], data['wake_time'], data['study_preference'],
                  data['noise_tolerance'], data['cleanliness_level'], data['social_preference'],
                  data['music_preference'], data['visitor_frequency'], data['temperature_preference'],
                  data['smoking_preference'], data['dietary_preferences'], data['interests'],
                  data['additional_notes'], session['student_id']))
        else:
            # Insert new preferences
            conn.execute('''
                INSERT INTO student_preferences (
                    student_id, sleep_time, wake_time, study_preference, noise_tolerance,
                    cleanliness_level, social_preference, music_preference, visitor_frequency,
                    temperature_preference, smoking_preference, dietary_preferences, interests,
                    additional_notes
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ''', (session['student_id'], data['sleep_time'], data['wake_time'],
                  data['study_preference'], data['noise_tolerance'], data['cleanliness_level'],
                  data['social_preference'], data['music_preference'], data['visitor_frequency'],
                  data['temperature_preference'], data['smoking_preference'],
                  data['dietary_preferences'], data['interests'], data['additional_notes']))
        
        conn.commit()
        conn.close()
        
        return jsonify({'success': True, 'message': 'Preferences saved successfully'})
        
    except Exception as e:
        return jsonify({'success': False, 'message': str(e)}), 500

@app.route('/api/allocate_rooms', methods=['POST'])
def api_allocate_rooms():
    """Allocate rooms based on preferences"""
    if 'admin_id' not in session:
        return jsonify({'success': False, 'message': 'Admin access required'}), 403
    
    try:
        allocation_system = RoomAllocationSystem(DATABASE_PATH)
        results = allocation_system.allocate_rooms()
        
        return jsonify({
            'success': True, 
            'message': f'Successfully allocated {results["allocated_count"]} students',
            'results': results
        })
        
    except Exception as e:
        return jsonify({'success': False, 'message': str(e)}), 500

@app.route('/api/students')
def api_get_students():
    """Get all students (admin only)"""
    if 'admin_id' not in session:
        return jsonify({'success': False, 'message': 'Admin access required'}), 403
    
    conn = get_db_connection()
    students = conn.execute('''
        SELECT s.*, sp.sleep_time, sp.study_preference, ra.room_id, r.room_number
        FROM students s
        LEFT JOIN student_preferences sp ON s.id = sp.student_id
        LEFT JOIN room_allocations ra ON s.id = ra.student_id AND ra.status = 'active'
        LEFT JOIN rooms r ON ra.room_id = r.id
    ''').fetchall()
    conn.close()
    
    return jsonify({'students': [dict(student) for student in students]})

@app.route('/api/rooms')
def api_get_rooms():
    """Get all rooms"""
    conn = get_db_connection()
    rooms = conn.execute('SELECT * FROM rooms').fetchall()
    conn.close()
    
    return jsonify({'rooms': [dict(room) for room in rooms]})

@app.route('/api/create_room', methods=['POST'])
def api_create_room():
    """Create a new room (admin only)"""
    if 'admin_id' not in session:
        return jsonify({'success': False, 'message': 'Admin access required'}), 403
    
    data = request.get_json()
    
    try:
        conn = get_db_connection()
        conn.execute('''
            INSERT INTO rooms (room_number, capacity, floor, building, amenities)
            VALUES (?, ?, ?, ?, ?)
        ''', (data['room_number'], data['capacity'], data['floor'], 
              data['building'], data['amenities']))
        conn.commit()
        conn.close()
        
        return jsonify({'success': True, 'message': 'Room created successfully'})
        
    except sqlite3.IntegrityError:
        return jsonify({'success': False, 'message': 'Room number already exists'}), 400
    except Exception as e:
        return jsonify({'success': False, 'message': str(e)}), 500

@app.route('/logout')
def logout():
    """Logout"""
    session.clear()
    return redirect(url_for('index'))

if __name__ == '__main__':
    init_db()
    app.run(debug=True, host='0.0.0.0', port=5000)