# DevBoard - خريطة هيكل المشروع الكامل

## 📂 هيكل الملفات والمجلدات

```
devboard/
│
├── pom.xml                              # ملف Maven - جميع الـ Dependencies
├── application.properties               # إعدادات التطبيق الرئيسية
├── README_AR.md                         # دليل المشروع بالعربية
│
└── src/main/java/com/devboard/
    │
    ├── DevBoardApplication.java         # نقطة البداية
    │
    ├── config/
    │   ├── SecurityConfiguration.java   # إعدادات الأمان والـ JWT
    │   └── SwaggerConfiguration.java    # إعدادات Swagger/OpenAPI
    │
    ├── controller/
    │   ├── AuthController.java          # متحكم المصادقة (Login/Register)
    │   ├── UserController.java          # متحكم المستخدمين
    │   ├── BoardController.java         # متحكم الألواح
    │   ├── BoardColumnController.java   # متحكم الأعمدة
    │   ├── TaskController.java          # متحكم المهام
    │   ├── CommentController.java       # متحكم التعليقات
    │   ├── ActivityLogController.java   # متحكم سجلات الأنشطة
    │   └── WebhookController.java       # متحكم Webhooks (GitHub)
    │
    ├── service/
    │   ├── UserService.java             # خدمة المستخدمين
    │   ├── UserMapper.java              # تحويل بيانات المستخدم
    │   ├── BoardService.java            # خدمة الألواح
    │   ├── BoardMapper.java             # تحويل بيانات الألواح
    │   ├── BoardColumnService.java      # خدمة الأعمدة
    │   ├── TaskService.java             # خدمة المهام
    │   ├── CommentService.java          # خدمة التعليقات
    │   ├── ActivityLogService.java      # خدمة سجلات الأنشطة
    │   └── WebhookService.java          # خدمة Webhooks
    │
    ├── repository/
    │   ├── UserRepository.java          # استعلامات المستخدمين
    │   ├── BoardRepository.java         # استعلامات الألواح
    │   ├── BoardColumnRepository.java   # استعلامات الأعمدة
    │   ├── TaskRepository.java          # استعلامات المهام
    │   ├── CommentRepository.java       # استعلامات التعليقات
    │   └── ActivityLogRepository.java   # استعلامات سجلات الأنشطة
    │
    ├── entity/
    │   ├── User.java                    # كيان المستخدم
    │   ├── Board.java                   # كيان اللوحة
    │   ├── BoardColumn.java             # كيان العمود
    │   ├── Task.java                    # كيان المهمة
    │   ├── Comment.java                 # كيان التعليق
    │   ├── ActivityLog.java             # كيان سجل النشاط
    │   ├── Priority.java                # تعداد الأولويات
    │   └── TaskStatus.java              # تعداد حالات المهام
    │
    ├── dto/
    │   ├── UserDTOs.java                # DTOs للمستخدمين
    │   ├── AuthDTOs.java                # DTOs للمصادقة
    │   ├── BoardDTOs.java               # DTOs للألواح
    │   ├── BoardColumnDTOs.java         # DTOs للأعمدة
    │   ├── TaskDTOs.java                # DTOs للمهام
    │   └── CommentDTOs.java             # DTOs للتعليقات
    │
    ├── security/
    │   ├── JwtTokenProvider.java        # مزود التوكنات
    │   ├── JwtAuthenticationFilter.java # فلتر المصادقة
    │   └── CustomUserDetailsService.java # خدمة تحميل المستخدم
    │
    ├── exception/
    │   ├── GlobalExceptionHandler.java  # معالج الأخطاء العام
    │   ├── ResourceNotFoundException.java # استثناء عدم العثور
    │   ├── BadRequestException.java     # استثناء الطلب الخاطئ
    │   ├── AccessDeniedException.java   # استثناء رفض الوصول
    │   ├── DuplicateResourceException.java # استثناء المورد المكرر
    │   └── ErrorResponse.java           # بنية رد الخطأ
    │
    └── util/                            # (اختياري) أدوات مساعدة
        └── DateUtil.java                # أدوات التاريخ والوقت
```

---

## 📊 علاقات الكيانات (Entity Relationships)

```
User (المستخدم)
├── 1:N → Task (creator)        [مهام أنشأها المستخدم]
├── 1:N → Task (assignee)       [مهام موكلة للمستخدم]
├── 1:N → Comment               [تعليقات الكاتب]
├── 1:N → ActivityLog           [سجلات النشاط]
├── M:N → Board (members)       [الألواح المشاركة فيها]
└── 1:N → Board (owner)         [الألواح المملوكة]

Board (اللوحة)
├── 1:N → BoardColumn           [الأعمدة]
├── 1:N → Task                  [المهام]
├── 1:N → ActivityLog           [سجلات النشاط]
├── M:N → User (members)        [أعضاء اللوحة]
└── 1:1 → User (owner)          [مالك اللوحة]

BoardColumn (العمود)
├── 1:N → Task                  [المهام في العمود]
└── 1:1 → Board                 [اللوحة التابعة لها]

Task (المهمة)
├── 1:N → Comment               [تعليقات المهمة]
├── 1:N → ActivityLog           [سجلات نشاط المهمة]
├── 1:1 → User (creator)        [منشئ المهمة]
├── 0:1 → User (assignee)       [الموكل إليه]
├── 1:1 → BoardColumn           [العمود التابعة له]
└── 1:1 → Board                 [اللوحة التابعة له]

Comment (التعليق)
├── 1:1 → User (author)         [كاتب التعليق]
├── 1:1 → Task                  [المهمة]
└── 0:1 → Comment (parent)      [التعليق الأب (للردود)]

ActivityLog (سجل النشاط)
├── 1:1 → User                  [المستخدم الذي قام بالعملية]
├── 0:1 → Board                 [اللوحة المتعلقة]
└── 0:1 → Task                  [المهمة المتعلقة]
```

---

## 🔐 معلومات الأمان

### JWT Token Structure
```
Header.Payload.Signature

Header: {
  "alg": "HS256",
  "typ": "JWT"
}

Payload: {
  "sub": "username",
  "iat": 1234567890,
  "exp": 1234671490
}

Signature: HMACSHA256(header.payload, secret)
```

### Password Encoding
- تشفير BCrypt بـ 10 rounds
- لا نخزن الكلمات الأصلية أبداً
- التحقق من الكلمة أثناء الدخول

### CORS Configuration
```
Allowed Origins:
- http://localhost:3000   (React)
- http://localhost:4200   (Angular)
- http://localhost:5173   (Vite)

Allowed Methods: GET, POST, PUT, DELETE, OPTIONS, PATCH
Allowed Headers: Content-Type, Authorization, etc.
Credentials: true
Max Age: 3600 seconds
```

---

## 📡 API Endpoints الرئيسية

### Authentication (العام)
```
POST   /api/auth/register         - التسجيل
POST   /api/auth/login            - الدخول
GET    /api/auth/validate         - التحقق من التوكن
POST   /api/auth/refresh          - تحديث التوكن
```

### Users (محمي)
```
GET    /api/users/me              - بيانات المستخدم الحالي
GET    /api/users/{userId}        - بيانات مستخدم معين
PUT    /api/users/{userId}        - تحديث المستخدم
POST   /api/users/{userId}/change-password - تغيير كلمة المرور
GET    /api/users/search          - البحث عن المستخدمين
GET    /api/users                 - جميع المستخدمين النشطين
```

### Boards (محمي)
```
POST   /api/boards                - إنشاء لوحة جديدة
GET    /api/boards                - ألواح المستخدم
GET    /api/boards/{boardId}      - لوحة معينة
GET    /api/boards/{boardId}/details - تفاصيل اللوحة
PUT    /api/boards/{boardId}      - تحديث اللوحة
DELETE /api/boards/{boardId}      - حذف اللوحة
POST   /api/boards/{boardId}/members - إضافة عضو
DELETE /api/boards/{boardId}/members/{userId} - إزالة عضو
```

### Columns (محمي)
```
POST   /api/boards/{boardId}/columns           - إضافة عمود
GET    /api/boards/{boardId}/columns           - أعمدة اللوحة
PUT    /api/columns/{columnId}                 - تحديث عمود
DELETE /api/columns/{columnId}                 - حذف عمود
POST   /api/columns/{columnId}/reorder         - إعادة ترتيب
```

### Tasks (محمي)
```
POST   /api/boards/{boardId}/tasks             - إنشاء مهمة
GET    /api/boards/{boardId}/tasks             - مهام اللوحة
GET    /api/tasks/{taskId}                     - مهمة معينة
PUT    /api/tasks/{taskId}                     - تحديث مهمة
DELETE /api/tasks/{taskId}                     - حذف مهمة
POST   /api/tasks/{taskId}/move                - نقل مهمة
POST   /api/tasks/{taskId}/assign              - تعيين مهمة
```

### Comments (محمي)
```
POST   /api/tasks/{taskId}/comments            - إضافة تعليق
GET    /api/tasks/{taskId}/comments            - تعليقات المهمة
PUT    /api/comments/{commentId}               - تحديث تعليق
DELETE /api/comments/{commentId}               - حذف تعليق
```

### Activity Logs (محمي)
```
GET    /api/boards/{boardId}/activity          - نشاط اللوحة
GET    /api/tasks/{taskId}/activity            - نشاط المهمة
GET    /api/users/{userId}/activity            - نشاط المستخدم
```

### Webhooks (عام)
```
POST   /api/webhooks/github                    - استقبال حدث GitHub
```

---

## 🔄 دورة الحياة الكاملة لمثال عملي

### سيناريو: إنشاء مهمة جديدة

```
1. المستخدم يرسل الطلب:
   POST /api/boards/1/tasks
   Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGc...
   Content-Type: application/json
   {
     "title": "Implement login",
     "description": "Add JWT authentication",
     "columnId": 1,
     "priority": "HIGH",
     "assigneeId": 2
   }

2. JwtAuthenticationFilter يتحقق من التوكن
   ✓ التوكن صحيح → المتابعة
   ✗ التوكن غير صحيح → 401 Unauthorized

3. TaskController يستقبل الطلب
   - يتحقق من @Valid annotations
   - يستدعي TaskService.createTask()

4. TaskService.createTask()
   - يتحقق من وجود اللوحة
   - يتحقق من وصول المستخدم
   - يتحقق من وجود العمود
   - يتحقق من وجود الموكل إليه (اختياري)
   - ينشئ كائن Task جديد

5. TaskRepository.save()
   - حفظ البيانات في قاعدة البيانات
   - تشغيل @PrePersist

6. ActivityLogService.logActivity()
   - تسجيل العملية في ActivityLog

7. Response يُرسل للمستخدم:
   {
     "id": 5,
     "title": "Implement login",
     "status": "TODO",
     "priority": "HIGH",
     "createdAt": "2024-01-15T10:30:00",
     ...
   }
```

---

## ⚙️ إعدادات قاعدة البيانات المهمة

### المفاتيح الأساسية
- `users.id` - المفتاح الأساسي
- `boards.id` - المفتاح الأساسي
- `tasks.id` - المفتاح الأساسي
- إلخ...

### الفهارس (Indexes)
```sql
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_board_owner ON boards(owner_id);
CREATE INDEX idx_task_board ON tasks(board_id);
CREATE INDEX idx_task_assignee ON tasks(assignee_id);
CREATE INDEX idx_comment_task ON comments(task_id);
CREATE INDEX idx_activity_board ON activity_logs(board_id);
CREATE INDEX idx_activity_user ON activity_logs(user_id);
CREATE INDEX idx_activity_created ON activity_logs(created_at);
```

### Constraints
```sql
-- Unique Constraints
UNIQUE(username)
UNIQUE(email)
UNIQUE(board_id, name) -- لا يمكن تكرار اسم العمود في نفس اللوحة

-- Foreign Key Constraints
FOREIGN KEY (user_id) REFERENCES users(id)
FOREIGN KEY (board_id) REFERENCES boards(id)
FOREIGN KEY (task_id) REFERENCES tasks(id)
... إلخ
```

---

## 📈 معايير الأداء المتوقعة

- **Response Time**: < 200ms (في 95% من الحالات)
- **Throughput**: 1000+ requests/second
- **Database Connections**: 20 connections max
- **Memory Usage**: < 500MB
- **CPU Usage**: < 30% بشكل عادي

---

## 🔄 التحديثات والصيانة

### النسخ الاحتياطية
```bash
# النسخ الاحتياطية اليومية
mysqldump -u devboard_user -p devboard > /backups/devboard_$(date +%Y%m%d).sql
```

### التنظيف الدوري
```sql
-- حذف سجلات الأنشطة القديمة
DELETE FROM activity_logs WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY);

-- تحرير مساحة البيانات
OPTIMIZE TABLE users, boards, tasks, comments, activity_logs;
```

---

## 🎯 الملاحظات الأخيرة

✅ **المشروع جاهز للإنتاج**
✅ **جميع الملفات منفصلة**
✅ **معمارية نظيفة وواضحة**
✅ **أمان عالي المستوى**
✅ **توثيق شامل**
✅ **متابع الأفضليات**

📅 **آخر تحديث**: 2024
📦 **الإصدار**: 1.0.0
🚀 **الحالة**: Production Ready
