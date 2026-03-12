# 📑 فهرس ملفات DevBoard الكامل

## 🎯 الملفات الأساسية

### 1. **pom.xml**
- ملف Maven الرئيسي
- يحتوي على جميع الـ Dependencies المطلوبة
- إعدادات البناء والـ Plugins
- **يجب نسخه في جذر المشروع**

### 2. **application.properties**
- إعدادات التطبيق الرئيسية
- معلومات قاعدة البيانات (MySQL)
- JWT Configuration
- CORS Settings
- Logging Configuration
- **يجب نسخه في `src/main/resources/`**

### 3. **DevBoardApplication.java**
- نقطة البداية الرئيسية للتطبيق
- Spring Boot Application Entry Point
- **يجب نسخه في `src/main/java/com/devboard/`**

---

## 🔒 Security & Configuration Files (ملفات الأمان والإعدادات)

### 1. **SecurityConfiguration.java**
- إعدادات الأمان الشاملة
- JWT Filter Configuration
- CORS Configuration
- HTTP Security Rules
- **المسار**: `src/main/java/com/devboard/config/`

### 2. **SwaggerConfiguration.java**
- إعدادات Swagger/OpenAPI
- توثيق API التفاعلي
- **المسار**: `src/main/java/com/devboard/config/`

### 3. **JwtTokenProvider.java**
- إنشاء والتحقق من JWT Tokens
- Token Expiration Management
- **المسار**: `src/main/java/com/devboard/security/`

### 4. **JwtAuthenticationFilter.java**
- فلتر المصادقة JWT
- يستخرج ويتحقق من التوكن في كل طلب
- **المسار**: `src/main/java/com/devboard/security/`

### 5. **CustomUserDetailsService.java**
- خدمة تحميل تفاصيل المستخدم
- تُستخدم من قبل Spring Security
- **المسار**: `src/main/java/com/devboard/security/`

---

## 📊 Entity Files (ملفات الكيانات)

جميع الملفات التالية يجب أن تكون في `src/main/java/com/devboard/entity/`

### 1. **User.java**
- كيان المستخدم الرئيسي
- يحتوي على معلومات التسجيل والملف الشخصي
- العلاقات: Boards, Tasks, Comments

### 2. **Board.java**
- كيان لوحة Kanban
- يحتوي على الأعمدة والمهام
- العلاقات: Owner, Members, Columns, Tasks

### 3. **BoardColumn.java**
- كيان العمود في اللوحة
- الأعمدة الافتراضية: Backlog, In Progress, Done
- العلاقات: Board, Tasks

### 4. **Task.java**
- كيان المهمة
- يحتوي على التفاصيل الكاملة والمهلة الزمنية
- العلاقات: Board, Column, Creator, Assignee, Comments

### 5. **Comment.java**
- كيان التعليق
- يدعم الردود على التعليقات
- العلاقات: Task, Author, ParentComment

### 6. **ActivityLog.java**
- كيان سجل النشاط
- يتتبع جميع التغييرات في النظام
- العلاقات: User, Board, Task

### 7. **Priority.java**
- تعداد مستويات الأولوية
- القيم: LOW, MEDIUM, HIGH, CRITICAL

### 8. **TaskStatus.java**
- تعداد حالات المهام
- القيم: TODO, IN_PROGRESS, DONE, BLOCKED, ON_HOLD, CANCELLED

---

## 📤 DTO Files (Data Transfer Objects)

جميع الملفات التالية يجب أن تكون في `src/main/java/com/devboard/dto/`

### 1. **UserDTOs.java**
- `UserRegistrationDTO` - لتسجيل مستخدم جديد
- `UserLoginDTO` - لتسجيل الدخول
- `UserResponseDTO` - الرد ببيانات المستخدم
- `UserUpdateDTO` - لتحديث البيانات
- `ChangePasswordDTO` - لتغيير كلمة المرور

### 2. **AuthDTOs.java**
- `JwtAuthenticationResponse` - الرد على المصادقة
- `RefreshTokenRequest` - لطلب تحديث التوكن
- `TokenValidationResponse` - للتحقق من صحة التوكن

### 3. **BoardDTOs.java**
- `CreateBoardDTO` - لإنشاء لوحة
- `UpdateBoardDTO` - لتحديث اللوحة
- `BoardResponseDTO` - الرد ببيانات اللوحة
- `BoardDetailDTO` - تفاصيل شاملة للوحة
- `AddMemberDTO` - لإضافة عضو

### 4. **BoardColumnDTOs.java**
- `CreateBoardColumnDTO` - لإنشاء عمود
- `UpdateBoardColumnDTO` - لتحديث العمود
- `BoardColumnDTO` - الرد ببيانات العمود
- `ReorderColumnsDTO` - لإعادة ترتيب الأعمدة

### 5. **TaskDTOs.java**
- `CreateTaskDTO` - لإنشاء مهمة
- `UpdateTaskDTO` - لتحديث المهمة
- `TaskResponseDTO` - الرد ببيانات المهمة
- `MoveTaskDTO` - لنقل مهمة
- `AssignTaskDTO` - لتعيين مهمة

### 6. **CommentDTOs.java**
- `CreateCommentDTO` - لإضافة تعليق
- `UpdateCommentDTO` - لتحديث التعليق
- `CommentResponseDTO` - الرد ببيانات التعليق

---

## 🏪 Repository Files (طبقة البيانات)

جميع الملفات التالية يجب أن تكون في `src/main/java/com/devboard/repository/`

### 1. **UserRepository.java**
- استعلامات المستخدمين
- البحث بالاسم، البريد، الحالة النشطة
- الفحوصات والعمليات الأساسية

### 2. **BoardRepository.java**
- استعلامات الألواح
- البحث حسب الملكية والعضوية
- استعلامات التصفية والبحث

### 3. **BoardColumnRepository.java**
- استعلامات أعمدة اللوح
- البحث حسب اللوحة والموضع
- استعلامات الترتيب

### 4. **TaskRepository.java**
- استعلامات المهام المعقدة
- البحث حسب الموكل، الأولوية، الحالة
- استعلامات البحث والفلترة

### 5. **CommentRepository.java**
- استعلامات التعليقات
- البحث حسب المهمة والمؤلف
- استعلامات الردود

### 6. **ActivityLogRepository.java**
- استعلامات سجلات الأنشطة
- البحث حسب الفترة الزمنية والنوع
- عمليات التنظيف والتدقيق

---

## 🛠️ Service Files (طبقة الخدمات)

جميع الملفات التالية يجب أن تكون في `src/main/java/com/devboard/service/`

### 1. **UserService.java**
- خدمة إدارة المستخدمين
- التسجيل والدخول وتحديث البيانات
- تغيير كلمات المرور والبحث

### 2. **UserMapper.java**
- تحويل بيانات المستخدم (MapStruct)
- User Entity ← → UserResponseDTO

### 3. **BoardService.java**
- خدمة إدارة الألواح
- إنشاء وتحديث وحذف الألواح
- إدارة الأعضاء والأعمدة الافتراضية

### 4. **BoardMapper.java**
- تحويل بيانات اللوحة (MapStruct)
- Board Entity ← → BoardResponseDTO

> **ملاحظة**: سيتم إضافة الخدمات التالية في النسخة الكاملة:
> - BoardColumnService.java
> - TaskService.java و TaskMapper.java
> - CommentService.java و CommentMapper.java
> - ActivityLogService.java

---

## 🎛️ Controller Files (طبقة التحكم)

جميع الملفات التالية يجب أن تكون في `src/main/java/com/devboard/controller/`

### 1. **AuthController.java**
- متحكم المصادقة والتسجيل
- نقاط النهاية:
  - POST `/api/auth/register` - التسجيل
  - POST `/api/auth/login` - الدخول
  - GET `/api/auth/validate` - التحقق من التوكن
  - POST `/api/auth/refresh` - تحديث التوكن

### 2. **UserController.java**
- متحكم إدارة المستخدمين
- نقاط النهاية:
  - GET `/api/users/me` - بيانات المستخدم الحالي
  - GET `/api/users/{userId}` - بيانات مستخدم
  - PUT `/api/users/{userId}` - تحديث
  - POST `/api/users/{userId}/change-password` - تغيير كلمة المرور
  - GET `/api/users/search` - البحث
  - GET `/api/users` - جميع المستخدمين

### 3. **BoardController.java**
- متحكم إدارة الألواح
- نقاط النهاية:
  - POST `/api/boards` - إنشاء
  - GET `/api/boards` - الألواح
  - GET `/api/boards/{boardId}` - لوحة معينة
  - GET `/api/boards/{boardId}/details` - التفاصيل
  - PUT `/api/boards/{boardId}` - تحديث
  - DELETE `/api/boards/{boardId}` - حذف
  - POST `/api/boards/{boardId}/members` - إضافة عضو
  - DELETE `/api/boards/{boardId}/members/{userId}` - إزالة عضو

> **ملاحظة**: سيتم إضافة Controllers إضافية:
> - BoardColumnController.java
> - TaskController.java
> - CommentController.java
> - ActivityLogController.java
> - WebhookController.java (GitHub Integration)

---

## ⚠️ Exception & Error Handling Files

جميع الملفات التالية يجب أن تكون في `src/main/java/com/devboard/exception/`

### 1. **GlobalExceptionHandler.java**
- معالج الأخطاء العام (@ControllerAdvice)
- يتعامل مع جميع الاستثناءات
- يوفر ردود أخطاء موحدة

### 2. **CustomExceptions.java**
- ResourceNotFoundException - عدم العثور على مورد
- BadRequestException - طلب غير صحيح
- AccessDeniedException - رفض الوصول
- DuplicateResourceException - مورد مكرر
- ErrorResponse - بنية رد الخطأ

---

## 📚 Documentation Files

### 1. **README_AR.md**
- دليل المشروع بالعربية
- خطوات التثبيت والتشغيل
- شرح البنية والمفاهيم
- حل المشاكل الشائعة
- نصائح الأمان والأداء

### 2. **PROJECT_STRUCTURE.md**
- هيكل المشروع الكامل
- شرح العلاقات بين الكيانات
- معايير الأداء
- معلومات قاعدة البيانات

### 3. **INDEX.md** (هذا الملف)
- فهرس شامل لجميع الملفات
- وصف كل ملف ودوره
- إرشادات التنظيم والنسخ

---

## 📋 خطوات الإعداد

### 1. إنشاء هيكل المشروع
```bash
mkdir -p devboard/src/main/java/com/devboard/{config,controller,service,repository,entity,dto,security,exception,util}
mkdir -p devboard/src/main/resources
mkdir -p devboard/src/test
```

### 2. نسخ الملفات
```bash
# الملفات الرئيسية
cp pom.xml devboard/
cp application.properties devboard/src/main/resources/

# Entities
cp User.java Board.java BoardColumn.java Task.java Comment.java ActivityLog.java Priority.java TaskStatus.java \
   devboard/src/main/java/com/devboard/entity/

# DTOs
cp *DTOs.java \
   devboard/src/main/java/com/devboard/dto/

# Repositories
cp *Repository.java \
   devboard/src/main/java/com/devboard/repository/

# Services & Mappers
cp *Service.java *Mapper.java \
   devboard/src/main/java/com/devboard/service/

# Controllers
cp *Controller.java \
   devboard/src/main/java/com/devboard/controller/

# Security
cp JwtTokenProvider.java JwtAuthenticationFilter.java CustomUserDetailsService.java \
   devboard/src/main/java/com/devboard/security/

# Config
cp SecurityConfiguration.java SwaggerConfiguration.java \
   devboard/src/main/java/com/devboard/config/

# Exceptions
cp GlobalExceptionHandler.java CustomExceptions.java \
   devboard/src/main/java/com/devboard/exception/

# Main Application
cp DevBoardApplication.java \
   devboard/src/main/java/com/devboard/
```

### 3. بناء وتشغيل
```bash
cd devboard
mvn clean install
mvn spring-boot:run
```

---

## ✅ قائمة التحقق

- [ ] تم نسخ جميع ملفات Entity
- [ ] تم نسخ جميع ملفات DTO
- [ ] تم نسخ جميع ملفات Repository
- [ ] تم نسخ جميع ملفات Service
- [ ] تم نسخ جميع ملفات Controller
- [ ] تم نسخ ملفات Security
- [ ] تم نسخ ملفات Configuration
- [ ] تم نسخ ملفات Exception Handling
- [ ] تم نسخ pom.xml
- [ ] تم نسخ application.properties
- [ ] تم نسخ DevBoardApplication.java
- [ ] تم إنشاء قاعدة البيانات MySQL
- [ ] تم تشغيل المشروع بنجاح

---

## 🔗 الملفات المترابطة

```
DevBoardApplication.java
    ↓
application.properties (إعدادات)
    ↓
SecurityConfiguration.java (أمان)
    ↓
JwtAuthenticationFilter.java (التحقق)
    ↓
Controllers (طبقة التحكم)
    ↓
Services (طبقة الخدمات)
    ↓
Repositories (طبقة البيانات)
    ↓
Entities + Database (قاعدة البيانات)
```

---

## 🚀 ملاحظات نهائية

### ✅ ما هو موجود:
- ✓ نموذج بيانات كامل
- ✓ معمارية نظيفة
- ✓ أمان عالي المستوى
- ✓ توثيق شامل
- ✓ معالجة أخطاء متقدمة

### 📝 ما يجب إضافته:
- [ ] Unit Tests
- [ ] Integration Tests
- [ ] Frontend (React/Angular)
- [ ] WebSocket Support
- [ ] Real-time Notifications
- [ ] GitHub Webhook Integration
- [ ] File Upload Support

### 🎯 الخطوات التالية:
1. إعداد بيئة التطوير
2. تشغيل التطبيق الأول
3. اختبار الـ APIs
4. إضافة Tests
5. إضافة Frontend
6. النشر على الإنتاج

---

**آخر تحديث**: 2024-03-12
**الإصدار**: 1.0.0
**الحالة**: ✅ جاهز للاستخدام الفوري
