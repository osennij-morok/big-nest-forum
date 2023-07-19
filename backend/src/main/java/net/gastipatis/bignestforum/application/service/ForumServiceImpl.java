package net.gastipatis.bignestforum.application.service;

import net.gastipatis.bignestforum.application.exception.EntityNotFoundException;
import net.gastipatis.bignestforum.application.exception.EntityValidationException;
import net.gastipatis.bignestforum.application.exception.GenericApplicationException;
import net.gastipatis.bignestforum.application.mapper.ForumMapper;
import net.gastipatis.bignestforum.application.mapper.PostMapper;
import net.gastipatis.bignestforum.application.mapper.ThreadMapper;
import net.gastipatis.bignestforum.application.validation.CreateForumRequestValidator;
import net.gastipatis.bignestforum.application.validation.PostInThreadRequestValidator;
import net.gastipatis.bignestforum.application.validation.StartThreadRequestValidator;
import net.gastipatis.bignestforum.data.ForumDAO;
import net.gastipatis.bignestforum.data.PostDAO;
import net.gastipatis.bignestforum.data.account.AccountRepository;
import net.gastipatis.bignestforum.domain.ForumEntity;
import net.gastipatis.bignestforum.domain.ForumPostEntity;
import net.gastipatis.bignestforum.domain.account.Account;
import net.gastipatis.bignestforum.dto.SeekingPage;
import net.gastipatis.bignestforum.dto.forum.CreateForumRequest;
import net.gastipatis.bignestforum.dto.forum.ForumDTO;
import net.gastipatis.bignestforum.dto.post.PostInThreadRequest;
import net.gastipatis.bignestforum.dto.post.ThreadPostDTO;
import net.gastipatis.bignestforum.dto.thread.ForumThreadDTO;
import net.gastipatis.bignestforum.dto.thread.StartThreadRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForumServiceImpl implements ForumService {

    private final ForumDAO forumDAO;
    private final PostDAO postDAO;
    private final AccountRepository accountRepository;
    private final ForumMapper forumMapper;
    private final PostMapper postMapper;
    private final ThreadMapper threadMapper;
    private final StartThreadRequestValidator startThreadRequestValidator;
    private final PostInThreadRequestValidator postInThreadRequestValidator;
    private final CreateForumRequestValidator createForumRequestValidator;

    public ForumServiceImpl(@Qualifier("default") ForumDAO forumDAO, PostDAO postDAO,
                            AccountRepository accountRepository, ForumMapper forumMapper,
                            PostMapper postMapper, ThreadMapper threadMapper,
                            StartThreadRequestValidator startThreadRequestValidator,
                            PostInThreadRequestValidator postInThreadRequestValidator,
                            CreateForumRequestValidator createForumRequestValidator) {
        this.forumDAO = forumDAO;
        this.postDAO = postDAO;
        this.accountRepository = accountRepository;
        this.forumMapper = forumMapper;
        this.postMapper = postMapper;
        this.threadMapper = threadMapper;
        this.startThreadRequestValidator = startThreadRequestValidator;
        this.postInThreadRequestValidator = postInThreadRequestValidator;
        this.createForumRequestValidator = createForumRequestValidator;
    }

    @Override
    public void startThread(StartThreadRequest request) {
        Errors errors = new BeanPropertyBindingResult(request, "startThreadRequest");
        ValidationUtils.invokeValidator(startThreadRequestValidator, request, errors);
        if (errors.hasErrors()) {
            throw new EntityValidationException(errors);
        }

        postDAO.newThread(request.getForumId(), getAccountIdByUsername(request.getSenderUsername()),
                request.getTopic(), request.getText());
    }

    @Override
    public void postInThread(PostInThreadRequest request) {
        Errors errors = new BeanPropertyBindingResult(request, "postInThreadRequest");
        ValidationUtils.invokeValidator(postInThreadRequestValidator, request, errors);
        if (errors.hasErrors()) {
            throw new EntityValidationException(errors);
        }

        postDAO.newPost(request.getForumId(), request.getThreadHeadPostId(),
                getAccountIdByUsername(request.getSenderUsername()), request.getTopic(), request.getText());
    }

    private Long getAccountIdByUsername(Optional<String> username) {
        return username
                .map(this::getAccountIdByUsername)
                .orElse(-1L);
    }

    private Long getAccountIdByUsername(String username) {
        return accountRepository.findAccountByUsername(username)
                .map(Account::getId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Account with username " + username + " does not exist"));
    }

    @Override
    public boolean deletePost(String forumId, long postId) {
        try {
            return postDAO.delete(forumId, postId);
        } catch (DataAccessException ex) {
            String message = String.format("Unable to delete post %d in forum %s", postId, forumId);
            throw new GenericApplicationException(message, ex);
        }
    }

    @Override
    public void createForum(CreateForumRequest request) {
        Errors errors = new BeanPropertyBindingResult(request, "createForumRequest");
        ValidationUtils.invokeValidator(createForumRequestValidator, request, errors);
        if (errors.hasErrors()) {
            throw new EntityValidationException(errors);
        }

        forumDAO.createForum(request.getId(), request.getCategoryId(), request.getName());
    }

    @Override
    public ForumDTO getForum(String forumId) throws EntityNotFoundException {
        ForumEntity forum = forumDAO.get(forumId);
        if (forum == null) {
            throw new EntityNotFoundException("Forum with id " + forumId + " does not exist");
        }
        return forumMapper.toDto(forum);
    }

    @Override
    public List<ForumDTO> listForums() {
        return forumDAO.get()
                .stream()
                .map(forumMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteForum(String forumId) {
        return forumDAO.delete(forumId);
    }

    @Override
    public List<ForumThreadDTO> listThreads(String forumId) {
        return postDAO.getAllThreadsInForum(forumId)
                .stream()
                .map(threadMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ThreadPostDTO> listPosts(String forumId, long threadId) {
        return postDAO.getAllThreadPosts(forumId, threadId)
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SeekingPage<ThreadPostDTO> listPostsPaged(String forumId, long threadId, long startId, long limit) {
        long postsTotalCount = postDAO.countPostsInThread(forumId, threadId);
        List<ThreadPostDTO> posts = postDAO.getPosts(forumId, threadId, startId, limit)
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
        return new SeekingPage<>(posts, postsTotalCount);
    }

    @Override
    public ThreadPostDTO getPost(String forumId, long postId) {
        ForumPostEntity post = postDAO.getPost(forumId, postId)
                .orElseThrow(() -> {
                    String message = String.format("The post in forum %s with id %d does not exist", forumId, postId);
                    return new EntityNotFoundException(message);
                });
        return postMapper.toDto(post);
    }
}
