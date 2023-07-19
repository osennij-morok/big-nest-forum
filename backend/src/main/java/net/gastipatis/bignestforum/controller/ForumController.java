package net.gastipatis.bignestforum.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import net.gastipatis.bignestforum.application.service.ForumService;
import net.gastipatis.bignestforum.application.exception.EntityNotFoundException;
import net.gastipatis.bignestforum.dto.forum.CreateForumRequest;
import net.gastipatis.bignestforum.dto.forum.ForumDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/forum")
public class ForumController {

    private final ForumService forumService;

    @GetMapping
    List<ForumDTO> getAll() {
        return forumService.listForums();
    }

    @GetMapping("/{id}")
    ForumDTO getById(@PathVariable String id) {
        return forumService.getForum(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    APIError handleEntityNotFound(EntityNotFoundException ex) {
        return new APIError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void create(@RequestBody CreateForumRequest forum) {
        forumService.createForum(forum);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@RequestParam @NotBlank String id) {
        if (!forumService.deleteForum(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    // TODO: create forum editing via @PutMapping
}
