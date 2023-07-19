package net.gastipatis.bignestforum.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import net.gastipatis.bignestforum.data.ForumCategoryDAO;
import net.gastipatis.bignestforum.domain.ForumCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Forum category")
@RestController
@RequestMapping("/category")
public class ForumCategoryController {

    private final ForumCategoryDAO forumCategoryDAO;

    public ForumCategoryController(ForumCategoryDAO forumCategoryDAO) {
        this.forumCategoryDAO = forumCategoryDAO;
    }

    @GetMapping
    Iterable<ForumCategory> getAll() {
        return forumCategoryDAO.findAll();
    }
}
