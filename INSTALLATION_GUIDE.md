# 📋 إرشادات نسخ الملفات وإعداد المشروع

## المرحلة 1️⃣: تحضير البيئة

### تثبيت المتطلبات

```bash
# على Linux (Ubuntu/Debian)
sudo apt-get update
sudo apt-get install openjdk-17-jdk maven mysql-server git

# على Mac
brew install openjdk@17 maven mysql git

# على Windows
# - قم بتحميل Java من: https://www.oracle.com/java/technologies/downloads/
# - قم بتحميل Maven من: https://maven.apache.org/download.cgi
# - قم بتحميل MySQL من: https://www.mysql.com/downloads/
# - قم بتحميل Git من: https://git-scm.com/download/win
```

### التحقق من التثبيت

```bash
java -version        # يجب أن يكون Java 17+
mvn -version        # يجب أن يكون Maven 3.6+
mysql --version     # يجب أن يكون MySQL 8.0+
```

---

## المرحلة 2️⃣: إعداد قاعدة البيانات

### تشغيل MySQL

```bash
# على Linux
sudo service mysql start

# على Mac
brew services start mysql

# على Windows - استخدم MySQL Command Line
```

### إنشاء قاعدة البيانات والمستخدم

```bash
# الدخول إلى MySQL
mysql -u root -p

# ثم نفذ الأوامر التالية:
CREATE DATABASE devboard CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'devboard_user'@'localhost' IDENTIFIED BY 'DevBoard@123Secure!';
GRANT ALL PRIVILEGES ON devboard.* TO 'devboard_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### التحقق من الاتصال

```bash
mysql -u devboard_user -p -h localhost
# أدخل كلمة المرور: DevBoard@123Secure!
SHOW DATABASES;  # يجب أن ترى devboard
EXIT;
```

---

## المرحلة 3️⃣: إنشاء هيكل المشروع

### الخطوة 1: إنشاء مجلدات المشروع

```bash
# أنشئ مجلد المشروع الرئيسي
mkdir -p ~/projects/devboard
cd ~/projects/devboard

# أنشئ هيكل المجلدات الكامل
mkdir -p src/main/java/com/devboard/{config,controller,service,repository,entity,dto,security,exception,util}
mkdir -p src/main/resources
mkdir -p src/test/java/com/devboard
mkdir -p target
mkdir -p logs

# تحقق من الهيكل
tree -L 3
```

---

## المرحلة 4️⃣: نسخ الملفات

### 1️⃣ نسخ الملفات الأساسية

```bash
# الملفات في جذر المشروع
cp /path/to/pom.xml ./
cp /path/to/application.properties ./src/main/resources/

# تحقق من النسخ
ls -la pom.xml
ls -la src/main/resources/application.properties
```

### 2️⃣ نسخ Entity Files

```bash
# انسخ جميع ملفات Entity
cp /path/to/User.java ./src/main/java/com/devboard/entity/
cp /path/to/Board.java ./src/main/java/com/devboard/entity/
cp /path/to/BoardColumn.java ./src/main/java/com/devboard/entity/
cp /path/to/Task.java ./src/main/java/com/devboard/entity/
cp /path/to/Comment.java ./src/main/java/com/devboard/entity/
cp /path/to/ActivityLog.java ./src/main/java/com/devboard/entity/
cp /path/to/Priority.java ./src/main/java/com/devboard/entity/
cp /path/to/TaskStatus.java ./src/main/java/com/devboard/entity/

# أو بطريقة أسرع:
cp /path/to/*.java ./src/main/java/com/devboard/entity/ \
  --filter='*Entity*' --filter='Priority*' --filter='TaskStatus*'

# تحقق
ls -la src/main/java/com/devboard/entity/ | wc -l
```

### 3️⃣ نسخ DTO Files

```bash
cp /path/to/*DTOs.java ./src/main/java/com/devboard/dto/

# تحقق
ls -la src/main/java/com/devboard/dto/
```

### 4️⃣ نسخ Repository Files

```bash
cp /path/to/*Repository.java ./src/main/java/com/devboard/repository/

# تحقق
ls -la src/main/java/com/devboard/repository/
```

### 5️⃣ نسخ Service Files

```bash
cp /path/to/*Service.java ./src/main/java/com/devboard/service/
cp /path/to/*Mapper.java ./src/main/java/com/devboard/service/

# تحقق
ls -la src/main/java/com/devboard/service/
```

### 6️⃣ نسخ Controller Files

```bash
cp /path/to/*Controller.java ./src/main/java/com/devboard/controller/

# تحقق
ls -la src/main/java/com/devboard/controller/
```

### 7️⃣ نسخ Security Files

```bash
cp /path/to/JwtTokenProvider.java ./src/main/java/com/devboard/security/
cp /path/to/JwtAuthenticationFilter.java ./src/main/java/com/devboard/security/
cp /path/to/CustomUserDetailsService.java ./src/main/java/com/devboard/security/

# تحقق
ls -la src/main/java/com/devboard/security/
```

### 8️⃣ نسخ Configuration Files

```bash
cp /path/to/SecurityConfiguration.java ./src/main/java/com/devboard/config/
cp /path/to/SwaggerConfiguration.java ./src/main/java/com/devboard/config/

# تحقق
ls -la src/main/java/com/devboard/config/
```

### 9️⃣ نسخ Exception Files

```bash
cp /path/to/GlobalExceptionHandler.java ./src/main/java/com/devboard/exception/
cp /path/to/CustomExceptions.java ./src/main/java/com/devboard/exception/

# تحقق
ls -la src/main/java/com/devboard/exception/
```

### 🔟 نسخ Application Main Class

```bash
cp /path/to/DevBoardApplication.java ./src/main/java/com/devboard/

# تحقق
ls -la src/main/java/com/devboard/DevBoardApplication.java
```

---

## المرحلة 5️⃣: التحقق من النسخ الكامل

### فحص جميع الملفات

```bash
# عد الملفات في كل مجلد
echo "Entity files:"; ls -1 src/main/java/com/devboard/entity/*.java | wc -l
echo "DTO files:"; ls -1 src/main/java/com/devboard/dto/*.java | wc -l
echo "Repository files:"; ls -1 src/main/java/com/devboard/repository/*.java | wc -l
echo "Service files:"; ls -1 src/main/java/com/devboard/service/*.java | wc -l
echo "Controller files:"; ls -1 src/main/java/com/devboard/controller/*.java | wc -l
echo "Security files:"; ls -1 src/main/java/com/devboard/security/*.java | wc -l
echo "Config files:"; ls -1 src/main/java/com/devboard/config/*.java | wc -l
echo "Exception files:"; ls -1 src/main/java/com/devboard/exception/*.java | wc -l

# العدد الكلي
find src/main/java -name "*.java" | wc -l
```

### التحقق من application.properties

```bash
cat src/main/resources/application.properties | grep -E "^(spring|jwt|app)" | wc -l
# يجب أن يكون هناك 20+ سطر
```

---

## المرحلة 6️⃣: البناء والتشغيل

### البناء الأول (Download Dependencies)

```bash
# سيحمل جميع الـ Dependencies
mvn clean install

# إذا واجهت مشكلة، جرب:
mvn clean install -DskipTests

# للتحقق من الأخطاء:
mvn compile
```

### إذا واجهت مشاكل في البناء

```bash
# امسح ذاكرة التخزين المؤقت
rm -rf ~/.m2/repository

# أعد المحاولة
mvn clean install -U

# أو استخدم --debug للمزيد من المعلومات
mvn clean install --debug
```

### التشغيل

```bash
# الطريقة الأولى: استخدام Maven
mvn spring-boot:run

# الطريقة الثانية: إنشاء JAR وتشغيله
mvn clean package -DskipTests
java -jar target/devboard-1.0.0.jar

# الطريقة الثالثة: استخدام IDE
# في IntelliJ: Right-click DevBoardApplication.java > Run
# في Eclipse: Right-click Project > Run As > Spring Boot App
```

---

## المرحلة 7️⃣: التحقق من التشغيل

### الاختبار الأساسي

```bash
# افتح terminal جديد وجرب:

# 1. تسجيل مستخدم جديد
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test@12345678",
    "confirmPassword": "Test@12345678",
    "firstName": "Test",
    "lastName": "User"
  }'

# يجب أن تحصل على رد بـ 201 Created

# 2. الدخول
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "test@example.com",
    "password": "Test@12345678"
  }'

# احفظ الـ accessToken من الرد

# 3. الاختبار بـ Token
TOKEN="<paste-token-here>"
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer $TOKEN"

# يجب أن ترى بيانات المستخدم
```

### الوصول إلى الواجهات

```
Backend:     http://localhost:8080
Swagger UI:  http://localhost:8080/api/swagger-ui.html
API Docs:    http://localhost:8080/api/v3/api-docs
Health:      http://localhost:8080/actuator/health (اختياري)
```

---

## المرحلة 8️⃣: معالجة المشاكل الشائعة

### مشكلة: Port 8080 مشغول

```bash
# على Linux/Mac: احصل على Process ID
lsof -i :8080
kill -9 <PID>

# أو غير الـ port في application.properties
# server.port=8081
```

### مشكلة: لا يمكن الاتصال بقاعدة البيانات

```bash
# تحقق من MySQL
mysql -u devboard_user -p

# تحقق من application.properties
cat src/main/resources/application.properties | grep datasource

# تأكد من:
# - MySQL running
# - Username صحيح
# - Password صحيح
# - Database موجود
```

### مشكلة: Maven لا يحمل Dependencies

```bash
# حاول مع Offline flag
mvn clean install -o

# أو امسح ذاكرة التخزين المؤقت
rm -rf ~/.m2/repository
mvn clean install -U
```

### مشكلة: Java Version Error

```bash
# تحقق من الإصدار
java -version

# اضبط JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64  # Linux
export JAVA_HOME=$(/usr/libexec/java_home -v 17)     # Mac

# تحقق
echo $JAVA_HOME
java -version
```

---

## المرحلة 9️⃣: الاختبارات الإضافية

### اختبار API مع Postman

```
1. استيراد URL: http://localhost:8080/api/v3/api-docs
2. Postman سيُنشئ Collection تلقائياً
3. استخدم JWT Token في Headers
4. اختبر جميع الـ Endpoints
```

### اختبار قاعدة البيانات

```bash
# الدخول إلى قاعدة البيانات
mysql -u devboard_user -p devboard

# عرض الجداول
SHOW TABLES;

# عرض البيانات
SELECT * FROM users;
SELECT * FROM boards;
SELECT * FROM tasks;

# عد الصفوف
SELECT COUNT(*) FROM users;
```

### فحص السجلات (Logs)

```bash
# عرض السجلات الحية
tail -f logs/devboard.log

# أو في معلومات التطبيق
# tail -f ~/path/to/spring/boot/logs/devboard.log
```

---

## المرحلة 🔟: الإعدادات الإضافية

### تفعيل Feature اختياري: الكشف عن الخطأ المفصل

في `application.properties`:
```properties
server.error.include-message=always
server.error.include-binding-errors=always
server.error.include-stacktrace=on_param
```

### تفعيل Actuator (اختياري)

في `pom.xml` أضف:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

ثم في `application.properties`:
```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

### تفعيل H2 Database (اختياري - للاختبار)

في `pom.xml`:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## ✅ قائمة التحقق النهائية

- [ ] Java 17+ مثبت
- [ ] Maven 3.6+ مثبت
- [ ] MySQL 8.0+ مثبت وتشغيل
- [ ] قاعدة البيانات devboard منشأة
- [ ] جميع الملفات نُسخت بشكل صحيح
- [ ] pom.xml يوجد في جذر المشروع
- [ ] application.properties يوجد في src/main/resources
- [ ] Maven build نجح (mvn clean install)
- [ ] التطبيق يعمل (mvn spring-boot:run)
- [ ] Swagger متاح (http://localhost:8080/api/swagger-ui.html)
- [ ] اختبار المستخدم الأول نجح
- [ ] قاعدة البيانات تحتوي على بيانات

---

## 🎯 الخطوة التالية

بعد التشغيل الناجح:

1. **استكشاف الـ API** من خلال Swagger
2. **إنشاء مجلدات وألواح** باستخدام الـ API
3. **فهم الكود** بقراءة التعليقات
4. **إضافة Controllers جديدة** (Board Columns, Tasks, Comments)
5. **كتابة Tests**
6. **إضافة Frontend**

---

## 📞 الدعم

- اقرأ `README_AR.md` للمزيد من المعلومات
- اقرأ `PROJECT_STRUCTURE.md` لفهم البنية
- اقرأ التعليقات داخل الكود

---

**🎉 تم! أنت الآن مستعد للبدء!**

```
Happy Coding! 🚀
```
