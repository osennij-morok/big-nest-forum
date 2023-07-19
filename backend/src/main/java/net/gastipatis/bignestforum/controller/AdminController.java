package net.gastipatis.bignestforum.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.gastipatis.bignestforum.application.admin.AdminSandbox;
import net.gastipatis.bignestforum.application.admin.CreateRandomThreads;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Administration")
@RestController
@RequestMapping("admin")
public class AdminController {

    private final AdminSandbox sandbox;

    public AdminController(AdminSandbox sandbox) {
        this.sandbox = sandbox;
    }

    @PostMapping("forum/{forumId}/threads/random")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void randomPosts(@PathVariable String forumId,
                     @RequestParam(defaultValue = "10") int threadsAmount,
                     @RequestParam(defaultValue = "50") int postsMin,
                     @RequestParam(defaultValue = "100") int postsMax) {
        sandbox.createRandomThreads(CreateRandomThreads.builder()
                .forumId(forumId)
                .threadsAmount(threadsAmount)
                .postsMin(postsMin)
                .postsMax(postsMax)
                .build());
    }

    @DeleteMapping("forum/{forumId}/threads")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAllThreads(@PathVariable String forumId) {
        sandbox.clearForum(forumId);
    }
}
