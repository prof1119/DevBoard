package com.devboard.dto;

import lombok.*;
import com.devboard.entity.Task;
import com.devboard.entity.User;
import java.time.LocalDateTime;
import java.util.*;

// ==================== USER DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    private String email;
    private String password;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserDto user;
}

// ==================== BOARD DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private Boolean isPublic;
    private String githubRepoUrl;
    private String githubRepoOwner;
    private String githubRepoName;
    private List<BoardColumnDto> columns;
    private List<BoardMemberDto> members;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequest {
    private String name;
    private String description;
    private Boolean isPublic;
    private String githubRepoOwner;
    private String githubRepoName;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {
    private String name;
    private String description;
    private Boolean isPublic;
}

// ==================== BOARD COLUMN DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnDto {
    private Long id;
    private String name;
    private Integer position;
    private String color;
    private List<TaskDto> tasks;
    private Integer taskCount;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnCreateRequest {
    private String name;
    private Integer position;
    private String color;
}

// ==================== TASK DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private UserDto assignedTo;
    private UserDto createdBy;
    private Integer position;
    private String dueDate;
    private Integer githubIssueNumber;
    private String githubIssueUrl;
    private Integer githubPrNumber;
    private String githubPrUrl;
    private String githubPrStatus;
    private String estimatedHours;
    private String actualHours;
    private List<String> labels;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {
    private String title;
    private String description;
    private String priority;
    private Long columnId;
    private Long assignedToId;
    private String dueDate;
    private String estimatedHours;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequest {
    private String title;
    private String description;
    private String priority;
    private String status;
    private Long assignedToId;
    private String dueDate;
    private String estimatedHours;
    private String actualHours;
    private List<Long> labelIds;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveTaskRequest {
    private Long taskId;
    private Long targetColumnId;
    private Integer newPosition;
}

// ==================== TASK COMMENT DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentDto {
    private Long id;
    private UserDto user;
    private String content;
    private Boolean isEdited;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentCreateRequest {
    private String content;
}

// ==================== LABEL DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelDto {
    private Long id;
    private String name;
    private String color;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelCreateRequest {
    private String name;
    private String color;
}

// ==================== BOARD MEMBER DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardMemberDto {
    private Long id;
    private UserDto user;
    private String role;
    private LocalDateTime addedAt;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBoardMemberRequest {
    private String email;
    private String role;
}

// ==================== BOARD STATISTICS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardStatisticsDto {
    private Long boardId;
    private Integer totalTasks;
    private Integer completedTasks;
    private Integer tasksInProgress;
    private Integer tasksBlocked;
    private Map<String, Integer> tasksByPriority;
    private Map<String, Integer> tasksByAssignee;
    private Double completionPercentage;
}

// ==================== GITHUB INTEGRATION DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubIssueDto {
    private Integer number;
    private String title;
    private String description;
    private String state;
    private String url;
    private String assignee;
    private List<String> labels;
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubPullRequestDto {
    private Integer number;
    private String title;
    private String description;
    private String state;
    private String url;
    private String assignee;
    private Boolean merged;
}

// ==================== WEBHOOK DTOS ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubWebhookPayload {
    private String action;
    private Object issue;
    private Object pullRequest;
    private Object repository;
    private Object sender;
}

// ==================== ERROR RESPONSE ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private String error;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;
}

// ==================== API RESPONSE WRAPPER ====================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
