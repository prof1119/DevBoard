# DevBoard Installation & Setup Guide

Complete step-by-step guide to get DevBoard up and running on your local machine.

## 📋 Prerequisites Check

Before starting, ensure you have:

```bash
# Check Java version (need 17+)
java -version

# Check Maven (for backend)
mvn -version

# Check Node.js (need 18+)
node -v
npm -v

# Check MySQL (need 8.0+)
mysql --version
```

If any of these are missing, install them before proceeding.

---

## 🗄️ Database Setup

### Option 1: Using Docker (Recommended for Development)

```bash
# Start MySQL in Docker
docker run -d \
  --name devboard-mysql \
  -e MYSQL_ROOT_PASSWORD=devboard123 \
  -e MYSQL_DATABASE=devboard \
  -e MYSQL_USER=devboard \
  -e MYSQL_PASSWORD=devboard123 \
  -p 3306:3306 \
  mysql:8.0
```

### Option 2: Using Local MySQL

```bash
# Connect to MySQL
mysql -u root -p

# Create database and user
CREATE DATABASE devboard CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'devboard'@'localhost' IDENTIFIED BY 'devboard123';
GRANT ALL PRIVILEGES ON devboard.* TO 'devboard'@'localhost';
FLUSH PRIVILEGES;

# Exit MySQL
EXIT;

# Import schema
mysql -u devboard -p devboard < database/schema.sql
```

### Verify Database Setup

```bash
# Connect to database
mysql -u devboard -p devboard

# List tables
SHOW TABLES;

# Check user exists
SELECT * FROM users LIMIT 1;

# Exit
EXIT;
```

---

## 🔧 Backend Setup

### Step 1: Navigate to Backend Directory

```bash
cd devboard/backend
```

### Step 2: Create Configuration Files

Copy the environment template:

```bash
cp ../.env.template ../.env
```

Edit `.env` with your settings:

```bash
# Edit the file and update:
# - Database credentials
# - JWT secret (use: openssl rand -hex 32)
# - GitHub settings (if integrating)
```

### Step 3: Build the Project

```bash
# Download all dependencies and compile
mvn clean install

# This will:
# ✓ Download Maven plugins
# ✓ Download project dependencies
# ✓ Compile source code
# ✓ Run tests
# ✓ Package JAR file
```

### Step 4: Run the Backend

```bash
# Option A: Using Maven
mvn spring-boot:run

# Option B: Run JAR directly
java -jar target/devboard-1.0.0.jar

# Backend should start at http://localhost:8080/api
```

### Step 5: Verify Backend is Running

```bash
# In another terminal, check health endpoint
curl http://localhost:8080/api/actuator/health

# Expected response:
# {"status":"UP"}

# View API documentation
# Open: http://localhost:8080/api/swagger-ui.html
```

---

## 💻 Frontend Setup

### Step 1: Navigate to Frontend Directory

```bash
cd devboard/frontend
```

### Step 2: Create Environment File

```bash
# Create .env file
echo "REACT_APP_API_URL=http://localhost:8080/api" > .env
```

### Step 3: Install Dependencies

```bash
# Install npm packages
npm install

# This will:
# ✓ Download all React packages
# ✓ Install TailwindCSS
# ✓ Install state management libraries
# Takes 2-5 minutes depending on internet speed
```

### Step 4: Start Development Server

```bash
# Start React development server
npm start

# Frontend should automatically open at http://localhost:3000
# If not, manually visit: http://localhost:3000
```

### Step 5: Verify Frontend is Running

- You should see the DevBoard login page
- Open browser DevTools (F12) and check Console for any errors
- The Network tab should show successful API calls to http://localhost:8080/api

---

## 🔐 First Time Setup

### Create Admin User

The database comes with sample users. For production, create your own:

```bash
# Option 1: Via API
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@devboard.local",
    "password": "SecurePassword123!",
    "fullName": "Admin User"
  }'

# Option 2: Via Database
mysql -u devboard -p devboard

INSERT INTO users (username, email, password_hash, full_name, role, is_active)
VALUES ('admin', 'admin@devboard.local', '$2a$10$...', 'Admin User', 'ADMIN', true);

# Password hash should be bcrypt-hashed password
```

### Login to Application

1. Open http://localhost:3000
2. Login with created credentials:
   - Email: admin@devboard.local
   - Password: SecurePassword123!
3. Create your first board!

---

## 🐳 Using Docker Compose (All-in-One)

### Start Everything with One Command

```bash
# From project root
docker-compose up -d

# This starts:
# ✓ MySQL database
# ✓ Spring Boot backend
# ✓ React frontend
# ✓ phpMyAdmin (optional)
```

### Check Services Status

```bash
# View running containers
docker-compose ps

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Access Services

```
Backend API:     http://localhost:8080/api
Frontend:        http://localhost:3000
API Swagger:     http://localhost:8080/api/swagger-ui.html
phpMyAdmin:      http://localhost:8081
MySQL:           localhost:3306 (devboard/devboard123)
```

### Stop Everything

```bash
# Stop containers (keep data)
docker-compose stop

# Stop and remove containers (keep data)
docker-compose down

# Stop and remove everything including data
docker-compose down -v
```

---

## 🚀 Creating Your First Board

### Via Web Interface

1. Login to http://localhost:3000
2. Click "New Board"
3. Fill in:
   - Board Name: "Q1 2024 Development"
   - Description: "Track all Q1 features and bugs"
   - GitHub Repo: (optional) "myusername/myrepo"
4. Click "Create Board"
5. Board will have default columns: Todo, In Progress, In Review, Done
6. Click "Add Task" to create your first task

### Via API

```bash
# Create a board
curl -X POST http://localhost:8080/api/boards \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Q1 2024 Development",
    "description": "Track all Q1 features",
    "isPublic": false,
    "githubRepoOwner": "myusername",
    "githubRepoName": "myrepo"
  }'

# Create a task
curl -X POST http://localhost:8080/api/boards/1/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Design login page",
    "description": "Create mockups for new login flow",
    "priority": "HIGH",
    "columnId": 1,
    "dueDate": "2024-03-31"
  }'
```

---

## 🔗 GitHub Integration Setup

### Step 1: Create GitHub OAuth App

1. Go to https://github.com/settings/developers
2. Click "New OAuth App"
3. Fill in:
   - **Application name**: DevBoard
   - **Homepage URL**: http://localhost:3000
   - **Authorization callback URL**: http://localhost:3000/auth/callback
4. Copy **Client ID** and **Client Secret**

### Step 2: Configure GitHub Webhooks

1. In GitHub repo: Settings → Webhooks → Add webhook
2. Fill in:
   - **Payload URL**: http://your-server/api/webhooks/github/events
   - **Content type**: application/json
   - **Secret**: (your webhook secret from .env)
   - **Events**: "Issues", "Pull Requests", "Pushes"
3. Click "Add webhook"

### Step 3: Connect to DevBoard

1. In DevBoard: Board Settings → GitHub Integration
2. Enter: `github-username/repo-name`
3. Click "Connect"
4. Allow DevBoard access when prompted

### Test GitHub Integration

```bash
# Create an issue in GitHub
# - DevBoard should auto-create a task linked to it

# Open a pull request
# - Task should be updated with PR status

# Merge the PR
# - Task should automatically move to "Done" column
```

---

## 🧪 Testing

### Backend Unit Tests

```bash
cd backend

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn test jacoco:report
open target/site/jacoco/index.html
```

### Frontend Tests

```bash
cd frontend

# Run all tests
npm test

# Run in watch mode
npm test -- --watch

# Run with coverage
npm test -- --coverage
```

### Manual Testing

Use Postman collection: `docs/DevBoard-API.postman_collection.json`

---

## 🔧 Troubleshooting

### Backend Issues

**Problem:** Port 8080 already in use
```bash
# Find process using port 8080
lsof -i :8080

# Kill process (on macOS/Linux)
kill -9 <PID>

# Or change port in application.properties:
# server.port=8081
```

**Problem:** MySQL connection refused
```bash
# Check MySQL is running
ps aux | grep mysql

# Check MySQL status
sudo service mysql status

# Start MySQL
sudo service mysql start
```

**Problem:** JAR file not found
```bash
# Rebuild the project
mvn clean install

# Check target directory
ls -la target/
```

### Frontend Issues

**Problem:** npm install fails
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

**Problem:** Cannot connect to backend API
```bash
# Check backend is running
curl http://localhost:8080/api/actuator/health

# Check .env file has correct API URL
cat .env

# Check browser console for exact error
# Open DevTools: F12 → Console
```

**Problem:** CORS errors
```bash
# Ensure .env has correct backend URL
REACT_APP_API_URL=http://localhost:8080/api

# Check application.properties has frontend URL
app.cors.allowed-origins=http://localhost:3000
```

---

## 📚 Project Structure Explanation

```
DevBoard/
├── backend/                      Java Spring Boot project
│   ├── src/main/java/
│   │   └── com/devboard/
│   │       ├── entity/           Database models
│   │       ├── repository/       Data access (Spring Data JPA)
│   │       ├── service/          Business logic
│   │       ├── controller/       REST endpoints
│   │       ├── dto/              Request/response objects
│   │       ├── security/         JWT authentication
│   │       └── webhook/          GitHub integration
│   ├── src/main/resources/
│   │   └── application.properties Configuration
│   └── pom.xml                   Maven dependencies
│
├── frontend/                     React project
│   ├── public/                   Static files
│   ├── src/
│   │   ├── components/           Reusable React components
│   │   ├── pages/                Page components
│   │   ├── services/             API client
│   │   ├── store/                Zustand state management
│   │   └── App.jsx               Main component
│   ├── package.json              npm dependencies
│   └── tailwind.config.js        Styling configuration
│
├── database/
│   └── schema.sql                Database initialization
│
├── docker-compose.yml            Docker orchestration
├── .gitignore                    Git ignore rules
├── .env.template                 Environment template
└── README.md                     Project documentation
```

---

## ✅ Verification Checklist

- [ ] Java 17+ installed
- [ ] Node.js 18+ installed
- [ ] MySQL 8.0+ running
- [ ] Database created and schema imported
- [ ] Backend builds successfully (`mvn clean install`)
- [ ] Backend starts without errors (`mvn spring-boot:run`)
- [ ] Frontend dependencies installed (`npm install`)
- [ ] Frontend starts successfully (`npm start`)
- [ ] Can access API: http://localhost:8080/api/actuator/health
- [ ] Can access frontend: http://localhost:3000
- [ ] Can login with sample credentials
- [ ] Can create a new board
- [ ] Can create and move tasks

---

## 📞 Getting Help

If you encounter issues:

1. **Check logs**: `docker-compose logs -f`
2. **Read error messages carefully** - they usually tell you what's wrong
3. **Check application.properties** - verify database credentials
4. **Check .env file** - verify environment variables
5. **Review GitHub Issues** - someone might have had the same problem
6. **Ask for help** - open an issue with:
   - Your error message
   - Steps to reproduce
   - Your OS and versions

---

## 🎉 Next Steps

Once everything is running:

1. **Create test data**: Add boards, tasks, and team members
2. **Connect GitHub**: Set up webhooks for integration
3. **Invite team members**: Share board with your team
4. **Customize**: Adjust labels, columns, and workflows for your needs
5. **Deploy**: Follow production deployment guide in README

---

**You're all set! Happy task management! 🚀**
