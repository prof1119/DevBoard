-- DevBoard Database Schema
-- ========================
-- Complete schema for a collaborative kanban board with GitHub integration

-- Users Table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    github_username VARCHAR(100),
    github_token VARCHAR(500),
    avatar_url VARCHAR(500),
    role ENUM('ADMIN', 'MEMBER', 'VIEWER') DEFAULT 'MEMBER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Boards Table (Kanban Boards)
CREATE TABLE boards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id BIGINT NOT NULL,
    is_public BOOLEAN DEFAULT FALSE,
    github_repo_url VARCHAR(500),
    github_repo_owner VARCHAR(100),
    github_repo_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_owner (owner_id),
    INDEX idx_github_repo (github_repo_owner, github_repo_name)
);

-- Board Columns (Swimlanes: Todo, In Progress, Review, Done)
CREATE TABLE board_columns (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    board_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    position INT NOT NULL,
    color VARCHAR(7),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    UNIQUE KEY unique_board_column (board_id, name),
    INDEX idx_board (board_id),
    INDEX idx_position (board_id, position)
);

-- Board Members (Who has access to which board)
CREATE TABLE board_members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    board_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role ENUM('OWNER', 'EDITOR', 'COMMENTER', 'VIEWER') DEFAULT 'EDITOR',
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_board_member (board_id, user_id),
    INDEX idx_board (board_id),
    INDEX idx_user (user_id)
);

-- Labels/Tags for tasks
CREATE TABLE labels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    board_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    color VARCHAR(7) DEFAULT '#0366d6',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    UNIQUE KEY unique_board_label (board_id, name),
    INDEX idx_board (board_id)
);

-- Tasks Table (Kanban Cards)
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    board_id BIGINT NOT NULL,
    column_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') DEFAULT 'MEDIUM',
    status ENUM('TODO', 'IN_PROGRESS', 'IN_REVIEW', 'DONE', 'BLOCKED') DEFAULT 'TODO',
    assigned_to BIGINT,
    created_by BIGINT NOT NULL,
    position INT NOT NULL,
    due_date DATE,
    github_issue_number INT,
    github_issue_url VARCHAR(500),
    github_pr_number INT,
    github_pr_url VARCHAR(500),
    github_pr_status VARCHAR(50),
    github_pr_merged_at TIMESTAMP,
    estimated_hours DECIMAL(5,2),
    actual_hours DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    FOREIGN KEY (column_id) REFERENCES board_columns(id) ON DELETE SET NULL,
    FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_board (board_id),
    INDEX idx_column (column_id),
    INDEX idx_assigned (assigned_to),
    INDEX idx_creator (created_by),
    INDEX idx_github_issue (github_issue_number),
    INDEX idx_status (status),
    INDEX idx_priority (priority)
);

-- Task Labels (Many-to-Many relationship)
CREATE TABLE task_labels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    label_id BIGINT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (label_id) REFERENCES labels(id) ON DELETE CASCADE,
    UNIQUE KEY unique_task_label (task_id, label_id),
    INDEX idx_task (task_id),
    INDEX idx_label (label_id)
);

-- Task Comments
CREATE TABLE task_comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    github_comment_id BIGINT,
    is_edited BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_task (task_id),
    INDEX idx_user (user_id),
    INDEX idx_created (created_at)
);

-- Task Attachments
CREATE TABLE task_attachments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_size BIGINT,
    uploaded_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_task (task_id)
);

-- Task Activity Log
CREATE TABLE task_activity_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    user_id BIGINT,
    action VARCHAR(50) NOT NULL,
    old_value VARCHAR(500),
    new_value VARCHAR(500),
    field_name VARCHAR(100),
    activity_type ENUM('CREATED', 'UPDATED', 'MOVED', 'ASSIGNED', 'COMMENTED', 'GITHUB_SYNC') DEFAULT 'UPDATED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_task (task_id),
    INDEX idx_user (user_id),
    INDEX idx_action (action),
    INDEX idx_created (created_at)
);

-- GitHub Webhook Events (Track webhook deliveries)
CREATE TABLE github_webhook_events (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    board_id BIGINT NOT NULL,
    github_event_type VARCHAR(50) NOT NULL,
    github_delivery_id VARCHAR(255) UNIQUE,
    payload JSON,
    processed BOOLEAN DEFAULT FALSE,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    INDEX idx_board (board_id),
    INDEX idx_event_type (github_event_type),
    INDEX idx_processed (processed)
);

-- Board Invitations
CREATE TABLE board_invitations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    board_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    role ENUM('OWNER', 'EDITOR', 'COMMENTER', 'VIEWER') DEFAULT 'EDITOR',
    token VARCHAR(255) UNIQUE,
    is_accepted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    INDEX idx_board (board_id),
    INDEX idx_email (email),
    INDEX idx_token (token)
);

-- Notifications
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    task_id BIGINT,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    action_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE SET NULL,
    INDEX idx_user (user_id),
    INDEX idx_read (is_read),
    INDEX idx_created (created_at)
);

-- ========================
-- SAMPLE DATA (Optional - for testing)
-- ========================

-- Sample Users
INSERT INTO users (username, email, password_hash, full_name, role) VALUES
('admin', 'admin@devboard.local', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Admin User', 'ADMIN'),
('john_dev', 'john@devboard.local', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'John Developer', 'MEMBER'),
('jane_dev', 'jane@devboard.local', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Jane Developer', 'MEMBER'),
('viewer_user', 'viewer@devboard.local', '$2a$10$N9qo8uLOickgx2ZMRZoMye', 'Viewer User', 'VIEWER');

-- Sample Board
INSERT INTO boards (name, description, owner_id, is_public, github_repo_owner, github_repo_name) VALUES
(1, 'Q1 2024 Development', 'Track features and bugs for Q1', 1, FALSE, 'myorg', 'my-app');

-- Sample Board Columns
INSERT INTO board_columns (board_id, name, position, color) VALUES
(1, 'Todo', 0, '#6f42c1'),
(1, 'In Progress', 1, '#fd7e14'),
(1, 'In Review', 2, '#ffc107'),
(1, 'Done', 3, '#28a745');

-- Sample Board Members
INSERT INTO board_members (board_id, user_id, role) VALUES
(1, 1, 'OWNER'),
(1, 2, 'EDITOR'),
(1, 3, 'EDITOR'),
(1, 4, 'VIEWER');

-- Sample Tasks
INSERT INTO tasks (board_id, column_id, title, description, priority, status, assigned_to, created_by, position) VALUES
(1, 1, 'Design new login page', 'Create mockups for the redesigned login flow', 'HIGH', 'TODO', 2, 1, 0),
(1, 2, 'Fix bug in dashboard', 'Charts not rendering correctly on mobile', 'CRITICAL', 'IN_PROGRESS', 3, 1, 0),
(1, 3, 'Write API documentation', 'Document REST endpoints and authentication', 'MEDIUM', 'IN_REVIEW', 2, 1, 0),
(1, 4, 'Release v1.0', 'Initial production release', 'CRITICAL', 'DONE', 1, 1, 0);
