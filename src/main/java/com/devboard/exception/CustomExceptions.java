package com.devboard.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * ResourceNotFoundException - استثناء عند عدم العثور على مورد
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * BadRequestException - استثناء لطلب غير صحيح
 */
class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * AccessDeniedException - استثناء عند رفض الوصول
 */
class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
    
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * DuplicateResourceException - استثناء عند محاولة إنشاء مورد مكرر
 */
class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * ErrorResponse - بنية الرد على الخطأ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
