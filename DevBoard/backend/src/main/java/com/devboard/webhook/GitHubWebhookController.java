package com.devboard.controller;

import com.devboard.service.GitHubService;
import com.devboard.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/webhooks/github")
@RequiredArgsConstructor
public class GitHubWebhookController {

    private final GitHubService gitHubService;
    private final ObjectMapper objectMapper;

    /**
     * Handle GitHub webhook events
     */
    @PostMapping("/events")
    public ResponseEntity<ApiResponse<String>> handleGitHubEvent(
            @RequestBody String payload,
            @RequestHeader("X-GitHub-Event") String eventType,
            @RequestHeader("X-GitHub-Delivery") String deliveryId,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            HttpServletRequest request) {

        try {
            log.info("Received GitHub webhook event: {} (Delivery ID: {})", eventType, deliveryId);

            // Verify webhook signature (optional but recommended)
            // validateWebhookSignature(payload, signature);

            // Parse the payload
            JsonNode webhookPayload = objectMapper.readTree(payload);

            // Process the event based on type
            switch (eventType) {
                case "issues":
                    handleIssueEvent(webhookPayload, deliveryId);
                    break;
                case "pull_request":
                    handlePullRequestEvent(webhookPayload, deliveryId);
                    break;
                case "push":
                    handlePushEvent(webhookPayload, deliveryId);
                    break;
                case "issue_comment":
                    handleIssueCommentEvent(webhookPayload, deliveryId);
                    break;
                default:
                    log.info("Unhandled webhook event type: {}", eventType);
            }

            return ResponseEntity.ok(ApiResponse.success(
                "Webhook processed successfully",
                "Webhook processed"
            ));

        } catch (IOException e) {
            log.error("Failed to parse webhook payload", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid payload"));
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error processing webhook: " + e.getMessage()));
        }
    }

    /**
     * Handle GitHub issue events
     */
    private void handleIssueEvent(JsonNode payload, String deliveryId) {
        String action = payload.get("action").asText();
        JsonNode issue = payload.get("issue");
        JsonNode repository = payload.get("repository");

        String repoOwner = repository.get("owner").get("login").asText();
        String repoName = repository.get("name").asText();
        int issueNumber = issue.get("number").asInt();
        String issueTitle = issue.get("title").asText();
        String issueState = issue.get("state").asText();
        String issueUrl = issue.get("html_url").asText();

        log.info("Processing issue event: action={}, repo={}/{}, issue=#{}", 
            action, repoOwner, repoName, issueNumber);

        try {
            switch (action) {
                case "opened":
                    gitHubService.handleIssueOpened(repoOwner, repoName, issueNumber, 
                        issueTitle, issueUrl);
                    break;
                case "closed":
                    gitHubService.handleIssueClosed(repoOwner, repoName, issueNumber);
                    break;
                case "edited":
                    gitHubService.handleIssueEdited(repoOwner, repoName, issueNumber, 
                        issueTitle);
                    break;
                default:
                    log.info("Unhandled issue action: {}", action);
            }
        } catch (Exception e) {
            log.error("Error handling issue event", e);
        }
    }

    /**
     * Handle GitHub pull request events
     */
    private void handlePullRequestEvent(JsonNode payload, String deliveryId) {
        String action = payload.get("action").asText();
        JsonNode pr = payload.get("pull_request");
        JsonNode repository = payload.get("repository");

        String repoOwner = repository.get("owner").get("login").asText();
        String repoName = repository.get("name").asText();
        int prNumber = pr.get("number").asInt();
        String prTitle = pr.get("title").asText();
        String prState = pr.get("state").asText();
        String prUrl = pr.get("html_url").asText();
        boolean merged = pr.get("merged").asBoolean();

        log.info("Processing pull request event: action={}, repo={}/{}, pr=#{}", 
            action, repoOwner, repoName, prNumber);

        try {
            switch (action) {
                case "opened":
                    gitHubService.handlePullRequestOpened(repoOwner, repoName, prNumber, 
                        prTitle, prUrl);
                    break;
                case "closed":
                    if (merged) {
                        gitHubService.handlePullRequestMerged(repoOwner, repoName, prNumber);
                    } else {
                        gitHubService.handlePullRequestClosed(repoOwner, repoName, prNumber);
                    }
                    break;
                case "synchronize":
                    gitHubService.handlePullRequestUpdated(repoOwner, repoName, prNumber);
                    break;
                default:
                    log.info("Unhandled PR action: {}", action);
            }
        } catch (Exception e) {
            log.error("Error handling pull request event", e);
        }
    }

    /**
     * Handle GitHub push events
     */
    private void handlePushEvent(JsonNode payload, String deliveryId) {
        JsonNode repository = payload.get("repository");
        String ref = payload.get("ref").asText();
        String branch = ref.replaceFirst("^refs/heads/", "");

        String repoOwner = repository.get("owner").get("login").asText();
        String repoName = repository.get("name").asText();

        log.info("Processing push event: repo={}/{}, branch={}", 
            repoOwner, repoName, branch);

        try {
            gitHubService.handlePush(repoOwner, repoName, branch, payload);
        } catch (Exception e) {
            log.error("Error handling push event", e);
        }
    }

    /**
     * Handle issue comment events
     */
    private void handleIssueCommentEvent(JsonNode payload, String deliveryId) {
        String action = payload.get("action").asText();
        JsonNode issue = payload.get("issue");
        JsonNode comment = payload.get("comment");
        JsonNode repository = payload.get("repository");

        String repoOwner = repository.get("owner").get("login").asText();
        String repoName = repository.get("name").asText();
        int issueNumber = issue.get("number").asInt();
        String commentBody = comment.get("body").asText();
        String commentUser = comment.get("user").get("login").asText();

        log.info("Processing issue comment event: action={}, repo={}/{}, issue=#{}", 
            action, repoOwner, repoName, issueNumber);

        try {
            if ("created".equals(action)) {
                gitHubService.handleIssueComment(repoOwner, repoName, issueNumber, 
                    commentBody, commentUser);
            }
        } catch (Exception e) {
            log.error("Error handling issue comment event", e);
        }
    }

    /**
     * Validate webhook signature (implement based on your security requirements)
     */
    private void validateWebhookSignature(String payload, String signature) {
        // TODO: Implement HMAC-SHA256 signature validation
        // String expectedSignature = "sha256=" + HmacSHA256(payload, webhookSecret);
        // if (!signature.equals(expectedSignature)) {
        //     throw new SecurityException("Invalid webhook signature");
        // }
    }
}

/**
 * GitHub API Service
 */
@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
class GitHubService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final TaskActivityLogRepository activityLogRepository;

    /**
     * Handle GitHub issue opened event
     */
    public void handleIssueOpened(String owner, String repo, int issueNumber, 
                                   String title, String url) {
        // Find or create task linked to this issue
        log.info("Handling issue opened: {}/{} #{}", owner, repo, issueNumber);
        // Implementation
    }

    /**
     * Handle GitHub issue closed event
     */
    public void handleIssueClosed(String owner, String repo, int issueNumber) {
        // Find task and mark as done
        var task = taskRepository.findByGitHubIssue(issueNumber, owner, repo);
        if (task.isPresent()) {
            Task t = task.get();
            t.setStatus(Task.TaskStatus.DONE);
            taskRepository.save(t);
            log.info("Task marked as done: {}", t.getId());
        }
    }

    /**
     * Handle GitHub issue edited event
     */
    public void handleIssueEdited(String owner, String repo, int issueNumber, String newTitle) {
        var task = taskRepository.findByGitHubIssue(issueNumber, owner, repo);
        if (task.isPresent()) {
            Task t = task.get();
            t.setTitle(newTitle);
            taskRepository.save(t);
            log.info("Task title updated: {}", t.getId());
        }
    }

    /**
     * Handle GitHub pull request opened event
     */
    public void handlePullRequestOpened(String owner, String repo, int prNumber, 
                                        String title, String url) {
        log.info("Handling PR opened: {}/{} #{}", owner, repo, prNumber);
        // Implementation
    }

    /**
     * Handle GitHub pull request closed event
     */
    public void handlePullRequestClosed(String owner, String repo, int prNumber) {
        var task = taskRepository.findByGitHubPullRequest(prNumber, owner, repo);
        if (task.isPresent()) {
            Task t = task.get();
            t.setGithubPrStatus("CLOSED");
            taskRepository.save(t);
            log.info("PR closed: {}", t.getId());
        }
    }

    /**
     * Handle GitHub pull request merged event
     */
    public void handlePullRequestMerged(String owner, String repo, int prNumber) {
        var task = taskRepository.findByGitHubPullRequest(prNumber, owner, repo);
        if (task.isPresent()) {
            Task t = task.get();
            t.setStatus(Task.TaskStatus.DONE);
            t.setGithubPrStatus("MERGED");
            t.setGithubPrMergedAt(java.time.LocalDateTime.now());
            taskRepository.save(t);
            log.info("Task completed via PR merge: {}", t.getId());
        }
    }

    /**
     * Handle GitHub pull request updated event
     */
    public void handlePullRequestUpdated(String owner, String repo, int prNumber) {
        log.info("Handling PR updated: {}/{} #{}", owner, repo, prNumber);
        // Implementation
    }

    /**
     * Handle GitHub push event
     */
    public void handlePush(String owner, String repo, String branch, JsonNode payload) {
        log.info("Handling push to {}/{}/{}", owner, repo, branch);
        // Implementation
    }

    /**
     * Handle issue comment event
     */
    public void handleIssueComment(String owner, String repo, int issueNumber, 
                                    String commentBody, String commentUser) {
        var task = taskRepository.findByGitHubIssue(issueNumber, owner, repo);
        if (task.isPresent()) {
            log.info("New comment on task {}: {}", task.get().getId(), commentUser);
            // Create task comment
        }
    }
}
