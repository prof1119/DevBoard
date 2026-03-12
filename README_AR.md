# DevBoard - دليل التثبيت والتشغيل

## 📋 نظرة عامة على المشروع

**DevBoard** هو تطبيق لوحة كانبان تعاونية احترافية (مشابه لـ Trello و Jira)، مبني باستخدام:
- **Backend**: Spring Boot 3.2.0، Java 17
- **Database**: MySQL 8.0+
- **Architecture**: Clean Architecture
- **Security**: JWT Authentication
- **Documentation**: Swagger/OpenAPI

---

## 🛠️ المتطلبات الأساسية

### أدوات التطوير
- **Java 17+** (JDK)
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**
- **IDE** (IntelliJ IDEA أو Eclipse أو VS Code)

### التثبيت على Linux/Mac
```bash
# تثبيت Java
sudo apt-get install openjdk-17-jdk  # Linux
brew install openjdk@17              # Mac

# تثبيت Maven
sudo apt-get install maven            # Linux
brew install maven                    # Mac

# تثبيت MySQL
sudo apt-get install mysql-server     # Linux
brew install mysql                    # Mac
```

### التثبيت على Windows
- قم بتحميل Java من: https://www.oracle.com/java/technologies/downloads/
- قم بتحميل Maven من: https://maven.apache.org/download.cgi
- قم بتحميل MySQL من: https://www.mysql.com/downloads/

---

## 📁 هيكل المشروع

```
devboard/
├── src/
│   └── main/
│       ├── java/com/devboard/
│       │   ├── config/              # إعدادات التطبيق
│       │   │   ├── SecurityConfiguration.java
│       │   │   └── SwaggerConfiguration.java
│       │   ├── controller/           # المتحكمات (HTTP Endpoints)
│       │   │   ├── AuthController.java
│       │   │   ├── UserController.java
│       │   │   ├── BoardController.java
│       │   │   └── ...
│       │   ├── service/             # طبقة الخدمات (Business Logic)
│       │   │   ├── UserService.java
│       │   │   ├── BoardService.java
│       │   │   └── ...
│       │   ├── repository/          # طبقة البيانات (Database Access)
│       │   │   ├── UserRepository.java
│       │   │   ├── BoardRepository.java
│       │   │   └── ...
│       │   ├── entity/              # كيانات JPA
│       │   │   ├── User.java
│       │   │   ├── Board.java
│       │   │   ├── Task.java
│       │   │   └── ...
│       │   ├── dto/                 # Data Transfer Objects
│       │   │   ├── UserDTOs.java
│       │   │   ├── BoardDTOs.java
│       │   │   └── ...
│       │   ├── security/            # المصادقة والأمان
│       │   │   ├── JwtTokenProvider.java
│       │   │   ├── JwtAuthenticationFilter.java
│       │   │   └── CustomUserDetailsService.java
│       │   ├── exception/           # معالجة الأخطاء
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   └── CustomExceptions.java
│       │   └── DevBoardApplication.java  # نقطة البداية
│       └── resources/
│           └── application.properties    # إعدادات التطبيق
└── pom.xml                          # ملف Maven

```

---

## 🚀 خطوات البدء السريع

### 1. تحضير قاعدة البيانات

```bash
# الاتصال بـ MySQL
mysql -u root -p

# تنفيذ الأوامر التالية
CREATE DATABASE devboard;
CREATE USER 'devboard_user'@'localhost' IDENTIFIED BY 'DevBoard@123Secure!';
GRANT ALL PRIVILEGES ON devboard.* TO 'devboard_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. استنساخ المشروع

```bash
git clone https://github.com/yourusername/devboard.git
cd devboard
```

### 3. تحميل الـ Dependencies

```bash
mvn clean install
```

### 4. تشغيل التطبيق

```bash
mvn spring-boot:run
```

أو باستخدام JAR:
```bash
mvn clean package
java -jar target/devboard-1.0.0.jar
```

التطبيق سيعمل على: `http://localhost:8080`

---

## 🔐 إعدادات الأمان

### تحديث `application.properties`

```properties
# قاعدة البيانات
spring.datasource.username=devboard_user
spring.datasource.password=YOUR_SECURE_PASSWORD

# JWT Secret - غير مهم جداً للأمان!
jwt.secret=YOUR_VERY_LONG_SECRET_KEY_MIN_32_CHARS

# عنوان CORS الموثوقة
app.cors.allowed-origins=http://localhost:3000,http://localhost:4200
```

### نصائح الأمان:
- ✅ استخدم كلمات مرور قوية
- ✅ غير JWT Secret في الإنتاج
- ✅ استخدم HTTPS في الإنتاج
- ✅ حدد CORS origins بدقة
- ✅ استخدم متغيرات البيئة للبيانات الحساسة

---

## 📚 API Documentation

بعد تشغيل التطبيق، يمكنك الوصول إلى التوثيق التفاعلي:

- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api/v3/api-docs`

---

## 🔑 نقاط API الرئيسية

### Authentication
```bash
# التسجيل
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe"
}

# الدخول
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "john@example.com",
  "password": "SecurePass123!"
}
```

### Boards
```bash
# إنشاء لوحة
POST /api/boards
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "name": "Q4 Project",
  "description": "Q4 Development",
  "isPublic": false
}

# الحصول على الألواح
GET /api/boards
Authorization: Bearer YOUR_JWT_TOKEN

# الحصول على لوحة معينة
GET /api/boards/{boardId}
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## 🧪 اختبار التطبيق

### اختبار سريع مع cURL

```bash
# 1. التسجيل
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test@123456",
    "confirmPassword": "Test@123456",
    "firstName": "Test",
    "lastName": "User"
  }'

# 2. الدخول
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "test@example.com",
    "password": "Test@123456"
  }'

# 3. استخدام التوكن
TOKEN="YOUR_JWT_TOKEN_HERE"
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🐛 حل المشاكل الشائعة

### مشكلة: Cannot find MySQL
```bash
# تحقق من حالة MySQL
sudo service mysql status

# أو
mysql -u root -p
```

### مشكلة: Port 8080 already in use
```bash
# غير الـ port في application.properties
server.port=8081
```

### مشكلة: Java version not found
```bash
# تحقق من الإصدار
java -version

# اضبط JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

### مشكلة: Authentication Failed
- تأكد من أن المستخدم موجود في قاعدة البيانات
- تحقق من كلمة المرور
- تأكد من أن المستخدم نشط

---

## 📊 شرح معمارية التطبيق

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│      HTTP Layer (Controllers)       │
├─────────────────────────────────────┤
│      Service Layer (Business)       │
├─────────────────────────────────────┤
│      Repository Layer (Data)        │
├─────────────────────────────────────┤
│      Database (MySQL)               │
└─────────────────────────────────────┘
```

### مسار الطلب:
1. **Controller**: يستقبل الطلب HTTP
2. **Service**: يعالج البيانات والمنطق التجاري
3. **Repository**: يتواصل مع قاعدة البيانات
4. **Entity**: يمثل بيانات قاعدة البيانات
5. **Response**: يُرسل الرد مرة أخرى

---

## 🔄 دورة حياة الطلب

```
1. المستخدم يرسل طلب HTTP
   ↓
2. SecurityFilter يتحقق من JWT Token
   ↓
3. Controller يستقبل الطلب
   ↓
4. Validation يتحقق من البيانات
   ↓
5. Service يعالج المنطق التجاري
   ↓
6. Repository يتفاعل مع قاعدة البيانات
   ↓
7. Response يُرسل الرد للمستخدم
```

---

## 📝 ملاحظات مهمة

### عن الملفات المنفصلة
- ✅ كل فئة في ملف منفصل (لا نجمع public classes)
- ✅ أسماء الملفات تطابق أسماء الفئات
- ✅ البنية منظمة حسب الحزم (Packages)

### عن الأمان
- ✅ JWT Tokens ثنائية التوقيع
- ✅ Passwords مشفرة مع BCrypt
- ✅ CORS معروّف بدقة
- ✅ Validation على جميع الإدخالات

### عن الأداء
- ✅ Lazy Loading للعلاقات
- ✅ Indexes على الأعمدة المهمة
- ✅ Connection Pooling
- ✅ Transaction Management

---

## 🚀 خطوات التطوير التالية

1. **إضافة Frontend** (React/Angular)
2. **إضافة Testing** (Unit & Integration)
3. **إضافة Caching** (Redis)
4. **إضافة Real-time Updates** (WebSocket)
5. **إضافة Notifications**
6. **GitHub Integration**
7. **CI/CD Pipeline**

---

## 📞 الدعم والمساعدة

- للمشاكل والأسئلة: افتح issue على GitHub
- للتحسينات: أرسل pull request
- للمزيد من المعلومات: اقرأ الـ inline comments في الكود

---

## 📄 الترخيص

هذا المشروع مرخص تحت Apache License 2.0

---

## ✅ قائمة التحقق قبل الإنتاج

- [ ] تغيير JWT Secret
- [ ] تحديث كلمة مرور قاعدة البيانات
- [ ] تفعيل HTTPS
- [ ] تحديث CORS origins
- [ ] تفعيل Logging المناسب
- [ ] إضافة Monitoring
- [ ] عمل Backup لقاعدة البيانات
- [ ] اختبار جميع الـ scenarios
- [ ] توثيق API كاملة
- [ ] إضافة Rate Limiting

---

**آخر تحديث**: 2024
**الإصدار**: 1.0.0
**الحالة**: جاهز للإنتاج ✅
