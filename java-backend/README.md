# Hostel Management System - Java Spring Boot Version

A comprehensive hostel management system built with **Java Spring Boot** featuring intelligent roommate matching based on student preferences and advanced compatibility scoring algorithms.

## 🚀 Technology Stack

- **Backend**: Java 17 + Spring Boot 3.1.5
- **Database**: H2 Database (development) / SQLite (production)
- **Frontend**: Thymeleaf + Bootstrap 5 + JavaScript
- **Security**: Spring Security with session management
- **Build Tool**: Maven
- **ORM**: Spring Data JPA with Hibernate

## ✨ Features

### 🎯 Core Functionality
- **Student Registration & Authentication**: Secure BCrypt password hashing
- **Smart Roommate Matching**: Advanced compatibility algorithm with 12+ factors
- **Comprehensive Questionnaire**: Detailed preference collection system
- **Admin Dashboard**: Complete system management interface
- **Room Management**: Dynamic room creation and occupancy tracking
- **Real-time Allocation**: Instant compatibility scoring and assignment

### 🧠 Intelligence Features
- **Multi-factor Compatibility Scoring** (0-100% scale)
- **Weighted Preference Matching** with customizable importance
- **Time-based Compatibility** for sleep/wake schedules
- **Social Personality Matching** (introvert/extrovert/ambivert)
- **Lifestyle Alignment** (cleanliness, noise tolerance, etc.)
- **Interest-based Bonuses** using Jaccard similarity
- **Minimum Threshold Filtering** (60% compatibility required)

## 📁 Project Structure

```
java-backend/
├── src/main/java/com/hostel/management/
│   ├── HostelManagementApplication.java    # Main Spring Boot class
│   ├── controller/                         # REST API controllers
│   │   ├── WebController.java             # Page routing
│   │   ├── StudentController.java         # Student operations
│   │   └── AdminController.java           # Admin operations
│   ├── entity/                            # JPA entities
│   │   ├── Student.java                   # Student entity
│   │   ├── StudentPreferences.java        # Preferences entity
│   │   ├── Room.java                      # Room entity
│   │   ├── RoomAllocation.java           # Allocation entity
│   │   └── Admin.java                     # Admin entity
│   ├── repository/                        # Data access layer
│   │   ├── StudentRepository.java
│   │   ├── RoomRepository.java
│   │   ├── StudentPreferencesRepository.java
│   │   ├── RoomAllocationRepository.java
│   │   └── AdminRepository.java
│   ├── service/                           # Business logic
│   │   └── RoomAllocationService.java     # Smart allocation algorithm
│   └── config/                            # Configuration classes
│       ├── SecurityConfig.java            # Spring Security setup
│       ├── WebConfig.java                 # Web MVC configuration
│       └── DataInitializer.java          # Sample data setup
├── src/main/resources/
│   ├── application.properties             # Application configuration
│   ├── templates/                         # Thymeleaf HTML templates
│   └── static/                           # CSS, JS, images
└── pom.xml                               # Maven dependencies
```

## 🔧 Setup & Installation

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **Git** (optional)

### Quick Start

1. **Clone or Navigate to Project**
   ```bash
   cd d:\Hostel-Management\java-backend
   ```

2. **Build the Application**
   ```bash
   mvn clean compile
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the System**
   - **Application**: http://localhost:8080
   - **H2 Console**: http://localhost:8080/h2-console (dev only)
   - **Admin Credentials**: `admin` / `admin123`

### Alternative Running Methods

#### Using JAR file:
```bash
mvn clean package
java -jar target/hostel-management-1.0.0.jar
```

#### Using IDE:
- Import Maven project
- Run `HostelManagementApplication.java`

## 📊 Database Configuration

### Development (H2 Database)
```properties
# Embedded H2 database for development
spring.datasource.url=jdbc:h2:file:./data/hostel_management
spring.h2.console.enabled=true
```

### Production Setup
For production, update `application.properties`:
```properties
# PostgreSQL example
spring.datasource.url=jdbc:postgresql://localhost:5432/hostel_management
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## 🎨 Frontend Integration

The system uses **Thymeleaf** templating with **Bootstrap 5** for responsive design:

- **Templates Location**: `src/main/resources/templates/`
- **Static Resources**: `src/main/resources/static/`
- **CSS Framework**: Bootstrap 5.1.3 + Custom CSS
- **JavaScript**: Vanilla JS with fetch API for AJAX calls

## 🔐 Security Features

- **BCrypt Password Hashing**: Secure password storage
- **Spring Security**: Authentication and authorization
- **Session Management**: HTTP session-based authentication
- **CORS Configuration**: Cross-origin request handling
- **CSRF Protection**: Configurable for API endpoints

## 🤖 Smart Allocation Algorithm

### Compatibility Factors & Weights:
```java
Sleep Time Compatibility:     15%  // Time-based matching
Wake Time Compatibility:      15%  // Morning/night person alignment
Study Preferences:           12%  // Group vs individual study
Noise Tolerance:             12%  // Quiet vs background noise
Cleanliness Level:           10%  // Organization compatibility
Social Preferences:          10%  // Introvert/extrovert matching
Music Preferences:            8%  // Volume and style compatibility
Visitor Frequency:            8%  // Guest policy alignment
Temperature Preferences:      5%  // Room climate preferences
Smoking Compatibility:       15%  // Critical lifestyle factor
Shared Interests Bonus:      10%  // Common hobbies/activities
```

### Algorithm Features:
- **Mathematical Time Compatibility**: Uses hour differences with wrap-around handling
- **Ordinal Scale Matching**: Smart scoring for low/medium/high preferences
- **Social Personality Matrix**: Predefined compatibility scores for personality types
- **Interest Similarity**: Jaccard coefficient for shared interests
- **Minimum Threshold**: 60% compatibility required for assignment
- **First-Come-First-Serve**: Registration date as tiebreaker

## 📋 API Endpoints

### Student Operations
```
POST /api/register          # Student registration
POST /api/login            # Student authentication
POST /api/preferences      # Save roommate preferences
GET  /api/students         # Get all students (admin only)
POST /api/logout          # Session logout
```

### Admin Operations
```
POST /api/admin_login      # Admin authentication
GET  /api/rooms           # Get all rooms
POST /api/create_room     # Create new room
POST /api/allocate_rooms  # Run allocation algorithm
```

### Web Pages
```
GET  /                    # Home page
GET  /register           # Student registration page
GET  /login             # Student login page
GET  /admin             # Admin login page
GET  /dashboard         # Student dashboard
GET  /admin_dashboard   # Admin dashboard
GET  /preferences       # Preference questionnaire
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
```bash
mvn test jacoco:report
```

### Integration Testing
The system includes integration tests for:
- REST API endpoints
- Database operations
- Allocation algorithm accuracy
- Security configurations

## ⚙️ Configuration Options

### Application Properties
```properties
# Server Configuration
server.port=8080
server.servlet.session.timeout=30m

# Database Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123

# Logging Configuration
logging.level.com.hostel.management=INFO
```

### Custom Allocation Settings
Modify `RoomAllocationService.java`:
```java
// Adjust minimum compatibility threshold
private static final double MINIMUM_COMPATIBILITY_SCORE = 60.0;

// Customize preference weights
private static final Map<String, Double> PREFERENCE_WEIGHTS = Map.of(
    "sleepTime", 0.15,
    "smokingPreference", 0.15  // High importance
    // ... other weights
);
```

## 🚀 Deployment

### Production Deployment

1. **Build Production JAR**
   ```bash
   mvn clean package -Pprod
   ```

2. **Configure Production Properties**
   ```bash
   java -jar target/hostel-management-1.0.0.jar \
     --spring.datasource.url=jdbc:postgresql://prod-db:5432/hostel \
     --spring.profiles.active=prod
   ```

3. **Docker Deployment** (Optional)
   ```dockerfile
   FROM openjdk:17-jre-slim
   COPY target/hostel-management-1.0.0.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/hostel
export SPRING_DATASOURCE_USERNAME=hostel_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export SERVER_PORT=8080
```

## 🔍 Monitoring & Logging

### Application Health
- **Actuator Endpoint**: `/actuator/health` (if enabled)
- **Database Status**: Automatic connection health checks
- **Memory Usage**: JVM metrics available

### Logging Configuration
```properties
# Enable SQL logging for debugging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Application-specific logging
logging.level.com.hostel.management=DEBUG
logging.level.org.springframework.security=INFO
```

## 🐛 Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Change port in application.properties
   server.port=8081
   ```

2. **Database Connection Issues**
   ```bash
   # Check H2 console at /h2-console
   # Verify JDBC URL and credentials
   ```

3. **Template Not Found**
   ```bash
   # Ensure templates are in src/main/resources/templates/
   # Check Thymeleaf configuration
   ```

4. **Static Resources Not Loading**
   ```bash
   # Verify files are in src/main/resources/static/
   # Check WebConfig.java resource handlers
   ```

### Development Tips

1. **Hot Reload**
   ```xml
   <!-- Add to pom.xml for development -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-devtools</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```

2. **Debug Mode**
   ```bash
   java -jar app.jar --debug
   ```

3. **Profile-specific Configuration**
   ```properties
   # application-dev.properties
   spring.jpa.show-sql=true
   logging.level.root=DEBUG
   ```

## 📈 Performance Optimization

### Database Optimization
- **Indexing**: Automatic indexes on foreign keys and unique constraints
- **Connection Pooling**: HikariCP (default in Spring Boot)
- **Query Optimization**: Use `@Query` annotations for complex queries

### Memory Management
- **JVM Tuning**: `-Xmx512m -Xms256m` for production
- **Garbage Collection**: G1GC recommended for low latency
- **Connection Limits**: Configure based on expected load

### Caching (Future Enhancement)
```java
// Add Spring Cache dependency
@Cacheable("student-preferences")
public StudentPreferences getPreferences(Long studentId) {
    // Cached method implementation
}
```

## 🔮 Future Enhancements

### Technical Improvements
- **Microservices Architecture**: Split into separate services
- **Message Queues**: Async processing with RabbitMQ/Kafka
- **Caching Layer**: Redis for session and data caching
- **API Documentation**: Swagger/OpenAPI integration
- **Monitoring**: Prometheus + Grafana dashboards

### Feature Additions
- **Email Notifications**: JavaMail integration
- **File Upload**: Profile pictures and documents
- **Advanced Analytics**: Detailed compatibility reports
- **Mobile API**: REST API for mobile applications
- **Feedback System**: Post-allocation satisfaction surveys

## 📝 Contributing

1. **Fork the Repository**
2. **Create Feature Branch**
   ```bash
   git checkout -b feature/new-feature
   ```
3. **Follow Code Standards**
   - Use Spring Boot conventions
   - Add comprehensive JavaDoc comments
   - Include unit tests for new features
4. **Submit Pull Request**

### Code Quality Tools
```bash
# Run checkstyle
mvn checkstyle:check

# Run SpotBugs
mvn spotbugs:check

# Generate reports
mvn site
```

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

- **Issues**: GitHub Issues for bug reports
- **Documentation**: JavaDoc generated with `mvn javadoc:javadoc`
- **Community**: Discussions in GitHub Discussions

---

**Java Spring Boot Version** - Built with enterprise-grade architecture and scalable design patterns for production-ready hostel management. 🏢✨