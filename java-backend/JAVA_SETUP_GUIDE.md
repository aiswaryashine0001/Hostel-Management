# Java Environment Setup Guide

This guide will help you set up the Java development environment required to run the Hostel Management System.

## 🎯 Prerequisites Installation

### 1. Java Development Kit (JDK) Installation

#### Option A: Eclipse Temurin (Recommended)
1. **Download JDK 17**
   - Visit: https://adoptium.net/temurin/releases/
   - Select: **OpenJDK 17 LTS**
   - Platform: **Windows x64**
   - Package Type: **JDK (.msi installer)**

2. **Install JDK**
   - Run the downloaded `.msi` file
   - Follow installation wizard (use default options)
   - Installation typically goes to: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot\`

3. **Verify Installation**
   ```cmd
   java -version
   javac -version
   ```

#### Option B: Oracle JDK
1. Download from: https://www.oracle.com/java/technologies/downloads/
2. Choose JDK 17 or higher
3. Follow installation instructions

### 2. Apache Maven Installation

#### Manual Installation
1. **Download Maven**
   - Visit: https://maven.apache.org/download.cgi
   - Download: `apache-maven-3.9.x-bin.zip`

2. **Extract Maven**
   - Extract to: `C:\Program Files\Apache\apache-maven-3.9.x`
   - Avoid paths with spaces if possible

3. **Set Environment Variables**
   - **JAVA_HOME**: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot`
   - **MAVEN_HOME**: `C:\Program Files\Apache\apache-maven-3.9.x`
   - **PATH**: Add `%MAVEN_HOME%\bin` and `%JAVA_HOME%\bin`

4. **Verify Installation**
   ```cmd
   mvn -version
   ```

#### Using Package Manager (Alternative)
```powershell
# Using Chocolatey (if installed)
choco install openjdk17
choco install maven

# Using Scoop (if installed)
scoop install openjdk17
scoop install maven
```

## 🔧 Environment Variables Setup

### Windows 10/11 GUI Method
1. **Open System Properties**
   - Press `Win + R`, type `sysdm.cpl`, press Enter
   - Click "Environment Variables"

2. **Set System Variables**
   - Click "New" under System Variables
   - Add these variables:
     ```
     Variable Name: JAVA_HOME
     Variable Value: C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot
     
     Variable Name: MAVEN_HOME
     Variable Value: C:\Program Files\Apache\apache-maven-3.9.x
     ```

3. **Update PATH Variable**
   - Select "Path" under System Variables, click "Edit"
   - Click "New" and add:
     ```
     %JAVA_HOME%\bin
     %MAVEN_HOME%\bin
     ```

### PowerShell Method (Temporary)
```powershell
# Set for current session only
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"
$env:MAVEN_HOME="C:\Program Files\Apache\apache-maven-3.9.x"
$env:PATH="$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:PATH"

# Verify
java -version
mvn -version
```

## 🚀 Quick Start After Installation

### 1. Verify Setup
```cmd
# Navigate to project directory
cd d:\Hostel-Management\java-backend

# Run setup verification
setup.bat
```

### 2. Build and Run
```cmd
# Option 1: Use batch file (recommended)
run-application.bat

# Option 2: Manual Maven commands
mvn clean compile
mvn spring-boot:run
```

### 3. Access Application
- **URL**: http://localhost:8080
- **Admin Login**: `admin` / `admin123`
- **H2 Database Console**: http://localhost:8080/h2-console

## 🛠 IDE Setup (Optional)

### IntelliJ IDEA
1. **Install IntelliJ IDEA Community** (free)
   - Download: https://www.jetbrains.com/idea/download/
2. **Open Project**
   - File → Open → Select `java-backend` folder
   - IntelliJ will auto-detect Maven project
3. **Run Configuration**
   - Run → Edit Configurations
   - Add → Application
   - Main Class: `com.hostel.management.HostelManagementApplication`

### Visual Studio Code
1. **Install Extensions**
   - Extension Pack for Java (Microsoft)
   - Spring Boot Extension Pack (VMware)
2. **Open Project**
   - File → Open Folder → Select `java-backend`
3. **Run Application**
   - Press F5 or use Command Palette → Java: Run

### Eclipse IDE
1. **Install Eclipse IDE for Java Developers**
   - Download: https://www.eclipse.org/downloads/
2. **Import Project**
   - File → Import → Maven → Existing Maven Projects
   - Browse to `java-backend` folder
3. **Run Application**
   - Right-click on project → Run As → Java Application
   - Select `HostelManagementApplication`

## 📋 Verification Checklist

After installation, verify these commands work:

```cmd
# Java verification
java -version
# Should show: openjdk version "17.x.x" or higher

javac -version  
# Should show: javac 17.x.x or higher

# Maven verification
mvn -version
# Should show Maven version and Java information

# Project verification (in java-backend directory)
mvn clean compile
# Should complete without errors

mvn spring-boot:run
# Should start the application on port 8080
```

## 🐛 Troubleshooting

### Common Issues

#### 1. "java is not recognized"
**Solution:**
- Verify JAVA_HOME is set correctly
- Ensure `%JAVA_HOME%\bin` is in PATH
- Restart command prompt/PowerShell
- Try: `where java` to see if Java is in PATH

#### 2. "mvn is not recognized"
**Solution:**
- Verify MAVEN_HOME is set correctly
- Ensure `%MAVEN_HOME%\bin` is in PATH
- Check Maven installation directory
- Restart command prompt

#### 3. "JAVA_HOME should point to a JDK not a JRE"
**Solution:**
- Download JDK (not JRE)
- Update JAVA_HOME to JDK path
- Ensure path includes `/bin` in PATH variable

#### 4. Compilation Errors
**Solution:**
- Verify Java version is 17 or higher
- Check all source files are present in `src/main/java`
- Try: `mvn clean` then `mvn compile`

#### 5. Port 8080 Already in Use
**Solutions:**
```cmd
# Check what's using port 8080
netstat -ano | findstr :8080

# Kill process using port (replace PID)
taskkill /F /PID <process_id>

# Or change port in application.properties
server.port=8081
```

#### 6. H2 Database Issues
**Solutions:**
- Check `application.properties` configuration
- Delete `data` folder and restart (will recreate database)
- Verify H2 console access: http://localhost:8080/h2-console

## 📚 Additional Resources

### Documentation
- **Java Documentation**: https://docs.oracle.com/en/java/javase/17/
- **Maven Documentation**: https://maven.apache.org/guides/
- **Spring Boot Documentation**: https://spring.io/projects/spring-boot

### Learning Resources
- **Java Tutorial**: https://docs.oracle.com/javase/tutorial/
- **Spring Boot Guide**: https://spring.io/guides/gs/spring-boot/
- **Maven Tutorial**: https://maven.apache.org/guides/getting-started/

### Community Support
- **Stack Overflow**: https://stackoverflow.com/questions/tagged/java
- **Spring Community**: https://spring.io/community
- **Maven Users**: https://maven.apache.org/community.html

## 🔄 Alternative Development Setups

### Using Docker (Advanced)
If you prefer containerized development:

```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY . .
RUN mvn clean compile
CMD ["mvn", "spring-boot:run"]
```

### Using GitHub Codespaces
1. Fork the repository on GitHub
2. Open in Codespaces
3. Environment comes pre-configured with Java and Maven

### Using Gitpod
1. Open project in Gitpod
2. Use `.gitpod.yml` for automatic environment setup

---

## ✅ Success Indicators

You'll know everything is working when:

1. **Commands work without errors:**
   ```cmd
   java -version    ✅
   mvn -version     ✅
   mvn compile      ✅
   mvn spring-boot:run ✅
   ```

2. **Application starts successfully:**
   ```
   Started HostelManagementApplication in X.XXX seconds
   ```

3. **Web interface is accessible:**
   - Homepage loads at http://localhost:8080
   - Admin login works with `admin`/`admin123`
   - Student registration page functions correctly

4. **Database operations work:**
   - H2 console accessible
   - Student registration saves data
   - Room allocation algorithm runs without errors

Once all these indicators are green, your Java development environment is ready for the Hostel Management System! 🎉