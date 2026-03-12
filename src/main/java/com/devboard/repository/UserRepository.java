package com.devboard.repository;


import com.devboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository - يتعامل مع عمليات قاعدة البيانات للمستخدمين
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * البحث عن مستخدم باسم المستخدم
     */
    Optional<User> findByUsername(String username);
    
    /**
     * البحث عن مستخدم بالبريد الإلكتروني
     */
    Optional<User> findByEmail(String email);
    
    /**
     * البحث عن مستخدم باسم المستخدم أو البريد الإلكتروني
     */
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    /**
     * التحقق من وجود مستخدم بهذا اسم المستخدم
     */
    boolean existsByUsername(String username);
    
    /**
     * التحقق من وجود مستخدم بهذا البريد الإلكتروني
     */
    boolean existsByEmail(String email);
    
    /**
     * البحث عن جميع المستخدمين النشطين
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.createdAt DESC")
    List<User> findAllActiveUsers();
    
    /**
     * البحث عن مستخدمين بناءً على جزء من الاسم
     */
    @Query("SELECT u FROM User u WHERE (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND u.isActive = true ORDER BY u.firstName")
    List<User> searchUsers(@Param("search") String search);
}
