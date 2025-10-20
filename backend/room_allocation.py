import sqlite3
import math
from typing import List, Dict, Tuple

class RoomAllocationSystem:
    def __init__(self, database_path: str):
        self.database_path = database_path
        
    def get_db_connection(self):
        """Get database connection"""
        conn = sqlite3.connect(self.database_path)
        conn.row_factory = sqlite3.Row
        return conn
    
    def calculate_compatibility_score(self, student1_prefs: Dict, student2_prefs: Dict) -> float:
        """Calculate compatibility score between two students based on their preferences"""
        
        # Define compatibility weights for different preferences
        weights = {
            'sleep_time': 0.15,
            'wake_time': 0.15,
            'study_preference': 0.12,
            'noise_tolerance': 0.12,
            'cleanliness_level': 0.10,
            'social_preference': 0.10,
            'music_preference': 0.08,
            'visitor_frequency': 0.08,
            'temperature_preference': 0.05,
            'smoking_preference': 0.15,  # High weight for smoking compatibility
        }
        
        total_score = 0.0
        total_weight = 0.0
        
        for pref_key, weight in weights.items():
            if pref_key in student1_prefs and pref_key in student2_prefs:
                if student1_prefs[pref_key] and student2_prefs[pref_key]:
                    score = self._calculate_preference_compatibility(
                        pref_key, student1_prefs[pref_key], student2_prefs[pref_key]
                    )
                    total_score += score * weight
                    total_weight += weight
        
        # Bonus for shared interests
        interests_score = self._calculate_interests_compatibility(
            student1_prefs.get('interests', ''), 
            student2_prefs.get('interests', '')
        )
        total_score += interests_score * 0.1
        total_weight += 0.1
        
        return (total_score / total_weight) * 100 if total_weight > 0 else 0.0
    
    def _calculate_preference_compatibility(self, pref_type: str, value1: str, value2: str) -> float:
        """Calculate compatibility for a specific preference type"""
        
        # Time-based preferences (sleep_time, wake_time)
        if pref_type in ['sleep_time', 'wake_time']:
            return self._time_compatibility(value1, value2)
        
        # Ordinal preferences (noise_tolerance, cleanliness_level)
        elif pref_type in ['noise_tolerance', 'cleanliness_level']:
            return self._ordinal_compatibility(value1, value2)
        
        # Binary preferences (smoking_preference)
        elif pref_type == 'smoking_preference':
            return 1.0 if value1 == value2 else 0.0
        
        # Categorical preferences
        else:
            return self._categorical_compatibility(value1, value2)
    
    def _time_compatibility(self, time1: str, time2: str) -> float:
        """Calculate compatibility for time preferences"""
        try:
            # Convert time strings to hours (24-hour format)
            hour1 = self._time_to_hour(time1)
            hour2 = self._time_to_hour(time2)
            
            # Calculate difference in hours
            diff = abs(hour1 - hour2)
            
            # Handle wrap-around (e.g., 23:00 and 01:00)
            if diff > 12:
                diff = 24 - diff
            
            # Score decreases as time difference increases
            # Perfect match (0 hours diff) = 1.0, 3+ hours diff = 0.0
            return max(0.0, 1.0 - (diff / 3.0))
            
        except:
            return 0.5  # Default score if time parsing fails
    
    def _time_to_hour(self, time_str: str) -> float:
        """Convert time string to hour value"""
        # Handle different time formats
        time_str = time_str.lower().strip()
        
        # Map common time descriptions to hours
        time_mappings = {
            'early morning': 6.0,
            'morning': 8.0,
            'late morning': 10.0,
            'noon': 12.0,
            'afternoon': 14.0,
            'evening': 18.0,
            'late evening': 20.0,
            'night': 22.0,
            'late night': 24.0,
            'midnight': 0.0
        }
        
        if time_str in time_mappings:
            return time_mappings[time_str]
        
        # Try to parse HH:MM format
        try:
            if ':' in time_str:
                parts = time_str.split(':')
                hour = float(parts[0])
                minute = float(parts[1]) / 60.0
                return hour + minute
        except:
            pass
        
        return 12.0  # Default to noon
    
    def _ordinal_compatibility(self, value1: str, value2: str) -> float:
        """Calculate compatibility for ordinal preferences (low/medium/high)"""
        ordinal_map = {'low': 1, 'medium': 2, 'high': 3}
        
        try:
            val1 = ordinal_map.get(value1.lower(), 2)
            val2 = ordinal_map.get(value2.lower(), 2)
            
            diff = abs(val1 - val2)
            
            # Perfect match = 1.0, one level diff = 0.5, two levels diff = 0.0
            return max(0.0, 1.0 - (diff / 2.0))
            
        except:
            return 0.5
    
    def _categorical_compatibility(self, value1: str, value2: str) -> float:
        """Calculate compatibility for categorical preferences"""
        # Define compatibility matrices for different preference types
        
        study_compatibility = {
            ('group', 'group'): 1.0,
            ('individual', 'individual'): 1.0,
            ('mixed', 'group'): 0.7,
            ('mixed', 'individual'): 0.7,
            ('mixed', 'mixed'): 1.0,
            ('group', 'individual'): 0.3,
            ('individual', 'group'): 0.3
        }
        
        social_compatibility = {
            ('extrovert', 'extrovert'): 1.0,
            ('introvert', 'introvert'): 1.0,
            ('ambivert', 'extrovert'): 0.8,
            ('ambivert', 'introvert'): 0.8,
            ('ambivert', 'ambivert'): 1.0,
            ('extrovert', 'introvert'): 0.4,
            ('introvert', 'extrovert'): 0.4
        }
        
        # Check study preferences
        study_key = (value1.lower(), value2.lower())
        if study_key in study_compatibility:
            return study_compatibility[study_key]
        
        # Check social preferences
        social_key = (value1.lower(), value2.lower())
        if social_key in social_compatibility:
            return social_compatibility[social_key]
        
        # Default: exact match = 1.0, different = 0.5
        return 1.0 if value1.lower() == value2.lower() else 0.5
    
    def _calculate_interests_compatibility(self, interests1: str, interests2: str) -> float:
        """Calculate compatibility based on shared interests"""
        if not interests1 or not interests2:
            return 0.5
        
        # Split interests by common delimiters
        int1_set = set(interest.strip().lower() for interest in 
                      interests1.replace(',', ';').split(';') if interest.strip())
        int2_set = set(interest.strip().lower() for interest in 
                      interests2.replace(',', ';').split(';') if interest.strip())
        
        if not int1_set or not int2_set:
            return 0.5
        
        # Calculate Jaccard similarity
        intersection = len(int1_set & int2_set)
        union = len(int1_set | int2_set)
        
        return intersection / union if union > 0 else 0.0
    
    def get_unallocated_students_with_preferences(self) -> List[Dict]:
        """Get students who haven't been allocated rooms and have filled preferences"""
        conn = self.get_db_connection()
        
        students = conn.execute('''
            SELECT s.*, sp.*
            FROM students s
            INNER JOIN student_preferences sp ON s.id = sp.student_id
            WHERE s.id NOT IN (
                SELECT student_id FROM room_allocations WHERE status = 'active'
            )
        ''').fetchall()
        
        conn.close()
        return [dict(student) for student in students]
    
    def get_available_rooms(self) -> List[Dict]:
        """Get rooms that have available space"""
        conn = self.get_db_connection()
        
        rooms = conn.execute('''
            SELECT r.*, 
                   COALESCE(COUNT(ra.student_id), 0) as current_occupancy
            FROM rooms r
            LEFT JOIN room_allocations ra ON r.id = ra.room_id AND ra.status = 'active'
            GROUP BY r.id
            HAVING current_occupancy < r.capacity
            ORDER BY current_occupancy ASC
        ''').fetchall()
        
        conn.close()
        return [dict(room) for room in rooms]
    
    def get_students_in_room(self, room_id: int) -> List[Dict]:
        """Get students currently allocated to a specific room"""
        conn = self.get_db_connection()
        
        students = conn.execute('''
            SELECT s.*, sp.*
            FROM students s
            INNER JOIN room_allocations ra ON s.id = ra.student_id
            INNER JOIN student_preferences sp ON s.id = sp.student_id
            WHERE ra.room_id = ? AND ra.status = 'active'
        ''', (room_id,)).fetchall()
        
        conn.close()
        return [dict(student) for student in students]
    
    def allocate_student_to_room(self, student_id: int, room_id: int, compatibility_score: float = 0.0):
        """Allocate a student to a room"""
        conn = self.get_db_connection()
        
        conn.execute('''
            INSERT INTO room_allocations (student_id, room_id, compatibility_score)
            VALUES (?, ?, ?)
        ''', (student_id, room_id, compatibility_score))
        
        # Update room occupancy
        conn.execute('''
            UPDATE rooms SET occupied = occupied + 1 WHERE id = ?
        ''', (room_id,))
        
        conn.commit()
        conn.close()
    
    def allocate_rooms(self) -> Dict:
        """Main room allocation algorithm"""
        students = self.get_unallocated_students_with_preferences()
        rooms = self.get_available_rooms()
        
        if not students:
            return {'allocated_count': 0, 'message': 'No students to allocate'}
        
        if not rooms:
            return {'allocated_count': 0, 'message': 'No available rooms'}
        
        allocated_count = 0
        allocation_details = []
        
        # Sort students by registration date (first come, first serve as tiebreaker)
        students.sort(key=lambda x: x['created_at'])
        
        for student in students:
            best_room = None
            best_score = -1
            
            for room in rooms:
                # Check if room has space
                if room['current_occupancy'] >= room['capacity']:
                    continue
                
                # Calculate compatibility with existing roommates
                roommates = self.get_students_in_room(room['id'])
                
                if not roommates:
                    # Empty room - good default choice
                    room_score = 75.0  # Base score for empty room
                else:
                    # Calculate average compatibility with all roommates
                    compatibility_scores = []
                    for roommate in roommates:
                        score = self.calculate_compatibility_score(student, roommate)
                        compatibility_scores.append(score)
                    
                    room_score = sum(compatibility_scores) / len(compatibility_scores)
                
                # Update best room if this is better
                if room_score > best_score:
                    best_score = room_score
                    best_room = room
            
            # Allocate to best room if found
            if best_room and best_score >= 60.0:  # Minimum compatibility threshold
                self.allocate_student_to_room(student['id'], best_room['id'], best_score)
                
                # Update room occupancy in our local data
                best_room['current_occupancy'] += 1
                
                allocation_details.append({
                    'student_name': student['name'],
                    'student_id': student['student_id'],
                    'room_number': best_room['room_number'],
                    'compatibility_score': round(best_score, 2)
                })
                
                allocated_count += 1
        
        return {
            'allocated_count': allocated_count,
            'total_students': len(students),
            'details': allocation_details,
            'message': f'Successfully allocated {allocated_count} out of {len(students)} students'
        }