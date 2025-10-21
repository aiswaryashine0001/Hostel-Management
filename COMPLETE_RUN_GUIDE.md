# 🚀 Complete Setup & Run Guide - Hostel Management System (Java)

## 📋 **Step-by-Step Installation & Running Guide**

### **STEP 1: Install Java (Required)**

#### Option A: Download & Install Java 17
1. **Go to**: https://adoptium.net/temurin/releases/
2. **Select**:
   - Version: **OpenJDK 17 (LTS)**
   - Operating System: **Windows**
   - Architecture: **x64**
   - Package Type: **JDK** (.msi installer)
3. **Download** the `.msi` file
4. **Run installer** and follow the wizard (use default settings)
5. **Installation location** will be: `C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot\`

#### Option B: Using Package Manager (if you have Chocolatey)
```powershell
# Open PowerShell as Administrator
choco install openjdk17
```

### **STEP 2: Install Maven (Required)**

#### Option A: Download & Install Maven
1. **Go to**: https://maven.apache.org/download.cgi
2. **Download**: `apache-maven-3.9.x-bin.zip`
3. **Extract to**: `C:\Program Files\Apache\apache-maven-3.9.x`
4. **Avoid paths with spaces**

#### Option B: Using Package Manager
```powershell
# Open PowerShell as Administrator
choco install maven
```

### **STEP 3: Set Environment Variables**

#### Method A: Using Windows GUI
1. **Press**: `Win + R`
2. **Type**: `sysdm.cpl` and press Enter
3. **Click**: "Environment Variables" button
4. **Under System Variables**, click "New" and add:
   ```
   Variable Name: JAVA_HOME
   Variable Value: C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot
   ```
5. **Add another**:
   ```
   Variable Name: MAVEN_HOME
   Variable Value: C:\Program Files\Apache\apache-maven-3.9.x
   ```
6. **Edit PATH variable**:
   - Select "Path" and click "Edit"
   - Click "New" and add: `%JAVA_HOME%\bin`
   - Click "New" and add: `%MAVEN_HOME%\bin`
7. **Click OK** on all dialogs
8. **Restart** your command prompt/PowerShell

#### Method B: Using PowerShell (Temporary for current session)
```powershell
# Set environment variables (temporary)
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"
$env:MAVEN_HOME="C:\Program Files\Apache\apache-maven-3.9.x"
$env:PATH="$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:PATH"
```

### **STEP 4: Verify Installation**

Open **new** PowerShell/Command Prompt and run:

```powershell
# Check Java installation
java -version
# Should show: openjdk version "17.x.x"

# Check Maven installation  
mvn -version
# Should show Maven version and Java information
```

### **STEP 5: Navigate to Project**

```powershell
# Go to your project directory
cd d:\Hostel-Management\java-backend
```

### **STEP 6: Run Setup Verification**

```powershell
# Run the setup script to verify everything
.\setup.bat
```

**Expected Output:**
```
✅ Java is installed
✅ Maven is installed
✅ Project structure looks good
✅ Setup completed successfully!
```

### **STEP 7: Start the Application**

#### Option A: Use the Run Script (Easiest)
```powershell
# Start the application using the batch file
.\run-application.bat
```

#### Option B: Manual Maven Commands
```powershell
# Compile the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

### **STEP 8: Access Your Application**

Once you see this message:
```
Started HostelManagementApplication in X.XXX seconds (JVM running for X.XXX)
```

**Open your browser and go to:**
- **Main Application**: http://localhost:8080
- **Admin Login**: http://localhost:8080/admin
- **H2 Database Console**: http://localhost:8080/h2-console

**Login Credentials:**
- **Admin Username**: `admin`
- **Admin Password**: `admin123`

### **STEP 9: Test the System**

1. **Home Page**: Should load with the background image
2. **Student Registration**: Click "Register as Student"
3. **Fill out registration form** and submit
4. **Login** with your new student account
5. **Complete preferences questionnaire**
6. **Admin Dashboard**: Login as admin to see all students and manage rooms

---

## 🛠 **Troubleshooting Common Issues**

### Issue 1: "java is not recognized"
**Solution:**
```powershell
# Check if JAVA_HOME is set
echo $env:JAVA_HOME

# If empty, set it manually:
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"

# Test again
java -version
```

### Issue 2: "mvn is not recognized"
**Solution:**
```powershell
# Check if MAVEN_HOME is set
echo $env:MAVEN_HOME

# If empty, set it manually:
$env:MAVEN_HOME="C:\Program Files\Apache\apache-maven-3.9.x"
$env:PATH="$env:MAVEN_HOME\bin;$env:PATH"

# Test again
mvn -version
```

### Issue 3: Port 8080 already in use
**Solution:**
```powershell
# Find what's using port 8080
netstat -ano | findstr :8080

# Kill the process (replace XXXX with actual PID)
taskkill /F /PID XXXX

# Or change port in application.properties
# Add this line: server.port=8081
```

### Issue 4: Compilation errors
**Solution:**
```powershell
# Clean and rebuild
mvn clean
mvn compile

# If still failing, check Java version
java -version
# Must be Java 17 or higher
```

### Issue 5: Application won't start
**Check these:**
1. Java version is 17+
2. No other application using port 8080
3. All files are present in the project directory
4. Run from the correct directory (`java-backend`)

---

## 🎯 **Quick Commands Reference**

```powershell
# Essential Commands (run these in order)
cd d:\Hostel-Management\java-backend    # Navigate to project
java -version                           # Verify Java installation
mvn -version                           # Verify Maven installation
mvn clean compile                      # Compile the project
mvn spring-boot:run                    # Start the application

# Alternative using batch files
.\setup.bat                            # Verify setup
.\run-application.bat                  # Start application
```

## 🌐 **Application URLs**

| Feature | URL | Credentials |
|---------|-----|------------|
| Home Page | http://localhost:8080 | - |
| Student Registration | http://localhost:8080/register | - |
| Student Login | http://localhost:8080/login | Your student account |
| Admin Dashboard | http://localhost:8080/admin | admin / admin123 |
| Database Console | http://localhost:8080/h2-console | See application.properties |

## 📁 **File Structure You Should Have**

```
d:\Hostel-Management\
├── java-backend/                    # ← Your Java application
│   ├── src/main/java/              # Java source code
│   ├── src/main/resources/         # Templates, CSS, JS
│   ├── pom.xml                     # Maven configuration
│   ├── setup.bat                   # Setup verification
│   ├── run-application.bat         # Application runner
│   └── README.md                   # Documentation
├── backend/                        # Old Python version (can keep)
├── frontend/                       # Old frontend (can keep)
└── .gitignore                      # Git ignore file
```

## 🚀 **Success Indicators**

You know everything is working when:

✅ **Commands work:**
- `java -version` shows Java 17+
- `mvn -version` shows Maven info
- `mvn compile` completes without errors

✅ **Application starts:**
- See "Started HostelManagementApplication" message
- No error messages in console

✅ **Web interface works:**
- Homepage loads at http://localhost:8080
- Can register new students
- Admin login works
- Database operations function

✅ **Features work:**
- Student registration saves data
- Preferences questionnaire works
- Room allocation algorithm runs
- Admin can manage rooms and students

---

## 💡 **Pro Tips**

1. **Always run from `java-backend` directory**
2. **Keep PowerShell/Command Prompt open** to see application logs
3. **Use `Ctrl+C`** to stop the application
4. **Check console output** for any error messages
5. **Use H2 console** to inspect database if needed

Once you complete all these steps, your complete Java hostel management system will be running! 🎉