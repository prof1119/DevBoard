# DevBoard DTOs Documentation

## Overview
This package contains all Data Transfer Objects (DTOs) for the DevBoard Spring Boot application. Each class has been separated into its own file for better organization and compilation.

## File Structure

### Board-Related DTOs
- **CreateBoardDTO.java** - Request DTO for creating a new board
- **UpdateBoardDTO.java** - Request DTO for updating an existing board
- **BoardResponseDTO.java** - Response DTO containing board information
- **BoardDetailDTO.java** - Response DTO with comprehensive board details including members and columns

### Board Column-Related DTOs
- **CreateBoardColumnDTO.java** - Request DTO for creating a new board column
- **UpdateBoardColumnDTO.java** - Request DTO for updating an existing column
- **BoardColumnDTO.java** - Response DTO containing column information
- **ReorderColumnsDTO.java** - Request DTO for reordering columns

### Task-Related DTOs
- **CreateTaskDTO.java** - Request DTO for creating a new task
- **UpdateTaskDTO.java** - Request DTO for updating an existing task
- **TaskResponseDTO.java** - Response DTO containing task information
- **MoveTaskDTO.java** - Request DTO for moving a task between columns
- **AssignTaskDTO.java** - Request DTO for assigning a task to a user

### Comment-Related DTOs
- **CreateCommentDTO.java** - Request DTO for creating a new comment
- **UpdateCommentDTO.java** - Request DTO for updating an existing comment
- **CommentResponseDTO.java** - Response DTO containing comment information

### User-Related DTOs
- **UserRegistrationDTO.java** - Request DTO for user registration
- **UserLoginDTO.java** - Request DTO for user login
- **LoginRequest.java** - Alternative name for user login (UserLoginDTO)
- **UserResponseDTO.java** - Response DTO containing user information
- **UserUpdateDTO.java** - Request DTO for updating user profile
- **ChangePasswordDTO.java** - Request DTO for changing password

### Authentication-Related DTOs
- **JwtAuthenticationResponse.java** - Response DTO containing JWT tokens and user info
- **RefreshTokenRequest.java** - Request DTO for refreshing JWT token
- **TokenValidationResponse.java** - Response DTO for token validation

### Special DTOs
- **AddMemberDTO.java** - Request DTO for adding a member to a board

## Installation

1. Extract the ZIP file
2. Copy all `.java` files to `src/main/java/com/devboard/dto/`
3. Ensure you have the following dependencies in your `pom.xml`:

```xml
<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <scope>provided</scope>
</dependency>

<!-- Jakarta Validation -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>

<!-- Springdoc OpenAPI (Swagger) -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.0</version>
</dependency>
```

4. Build your project: `mvn clean compile`

## Total Files
- **27 Java DTO Classes**
- **1 README.md**

## Usage Example

```java
// Creating a new board
CreateBoardDTO createBoardDTO = CreateBoardDTO.builder()
    .name("Q4 Planning")
    .description("Q4 product roadmap")
    .isPublic(false)
    .build();

// Creating a user
UserRegistrationDTO userDTO = UserRegistrationDTO.builder()
    .username("john_doe")
    .email("john@example.com")
    .password("SecurePass123!")
    .confirmPassword("SecurePass123!")
    .firstName("John")
    .lastName("Doe")
    .build();
```

## Validation
All DTOs include validation annotations from `jakarta.validation.constraints`:
- `@NotBlank` - Field is required and not empty
- `@NotNull` - Field must not be null
- `@Size` - Field size constraints
- `@Email` - Email format validation

## Notes
- All DTOs use Lombok annotations for automatic getter/setter/constructor generation
- All DTOs include Swagger/OpenAPI annotations for API documentation
- All DTOs are properly documented with JavaDoc comments
- Package name: `com.devboard.dto`

---
Generated: 2024
