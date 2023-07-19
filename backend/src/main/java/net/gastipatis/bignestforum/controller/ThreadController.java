package net.gastipatis.bignestforum.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.gastipatis.bignestforum.application.service.ForumService;
import net.gastipatis.bignestforum.application.exception.EntityNotFoundException;
import net.gastipatis.bignestforum.application.exception.GenericApplicationException;
import net.gastipatis.bignestforum.dto.*;
import net.gastipatis.bignestforum.dto.post.PostInThreadRequest;
import net.gastipatis.bignestforum.dto.post.PostInThreadRequestBody;
import net.gastipatis.bignestforum.dto.post.ThreadPostDTO;
import net.gastipatis.bignestforum.dto.thread.StartThreadRequest;
import net.gastipatis.bignestforum.dto.thread.StartThreadRequestBody;
import net.gastipatis.bignestforum.dto.thread.ForumThreadDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Posts")
@RestController
@RequestMapping("/")
public class ThreadController {

    private final ForumService forumService;

    public ThreadController(ForumService forumService) {
        this.forumService = forumService;
    }

    @Deprecated
    @GetMapping("/hello")
    String getGreeting() {
        return "Hello, World!";
    }

    @GetMapping("/thread")
    List<ForumThreadDTO> listThreads(@RequestParam String forumId) {
        return forumService.listThreads(forumId);
    }

    @GetMapping("/post")
    ResponseEntity<?> listPosts(@RequestParam String forumId,
                                @RequestParam(required = false) Long threadId,
                                @RequestParam(required = false) Long postId) {
        if (threadId == null && postId != null) {
            return ResponseEntity.ok(forumService.getPost(forumId, postId));
        }
        if (threadId != null && postId == null) {
            return ResponseEntity.ok(forumService.listPosts(forumId, threadId));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/post/pages")
    SeekingPage<ThreadPostDTO> postsPages(@RequestParam String forumId,
                                          @RequestParam Long threadId,
                                          @RequestParam(defaultValue = "1") Long fromId,
                                          @RequestParam(defaultValue = "30") Long limit) {
        return forumService.listPostsPaged(forumId, threadId, fromId, limit);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(new APIError(status.value(), ex.getMessage()));
    }

    @PostMapping("/{forumId}/post")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void newPost(@RequestBody PostInThreadRequestBody post,
                 @PathVariable String forumId) {
        PostInThreadRequest request = post.intoRequest(forumId, getAuthenticatedUsername());
        forumService.postInThread(request);
    }

    private Optional<String> getAuthenticatedUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .filter(username -> !username.equals("anonymousUser"));
    }

    @PostMapping("/{forumId}/thread")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void newThread(@RequestBody StartThreadRequestBody thread,
                   @PathVariable String forumId) {
        StartThreadRequest request = thread.intoRequest(forumId, getAuthenticatedUsername());
        forumService.startThread(request);
    }

    @DeleteMapping("/{forumId}/post")
    ResponseEntity<Void> deletePost(@PathVariable String forumId, @RequestParam Long postId) {
        try {
            forumService.deletePost(forumId, postId);
        } catch (GenericApplicationException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{forumId}/thread")
    ResponseEntity<Void> deleteThread(@PathVariable String forumId, @RequestParam Long threadId) {
        return deletePost(forumId, threadId);
    }
}
