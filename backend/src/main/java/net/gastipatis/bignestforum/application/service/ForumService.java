package net.gastipatis.bignestforum.application.service;

import net.gastipatis.bignestforum.application.exception.EntityNotFoundException;
import net.gastipatis.bignestforum.application.exception.GenericApplicationException;
import net.gastipatis.bignestforum.dto.*;
import net.gastipatis.bignestforum.dto.forum.CreateForumRequest;
import net.gastipatis.bignestforum.dto.forum.ForumDTO;
import net.gastipatis.bignestforum.dto.post.PostInThreadRequest;
import net.gastipatis.bignestforum.dto.post.ThreadPostDTO;
import net.gastipatis.bignestforum.dto.thread.StartThreadRequest;
import net.gastipatis.bignestforum.dto.thread.ForumThreadDTO;

import java.util.List;

public interface ForumService {

    /**
     * Start a thread.
     */
    void startThread(StartThreadRequest request);

    /**
     * Make a post.
//     * @param forumId the forum the target thread is located in
//     * @param threadHeadPostId the thread to post in head post id
//     * @param topic the post topic
//     * @param text the post text
     */
    void postInThread(PostInThreadRequest request);

    /**
     * Delete a post by the specified ID. For moderators and admins only.
     * @param forumId ID of the forum the post is located in
     * @param postId ID of the post to be deleted
     */
    boolean deletePost(String forumId, long postId) throws GenericApplicationException;

    /**
     * Create a new forum. For admins only.
//     * @param forumId the new forum ID. Must contain only latin alphanumeric characters
//     * @param categoryId the new forum category id
//     * @param forumFullName the new forum full name that describes the forum topic
     */
    void createForum(CreateForumRequest request);

    /**
     * Get forum by id
     * @param forumId the forum id
     * @return forum stored by the id
     * @throws EntityNotFoundException if forum with such ID does not exist
     */
    ForumDTO getForum(String forumId) throws EntityNotFoundException;

    /**
     * Get all forums
     * @return forums List
     */
    List<ForumDTO> listForums();

    /**
     * Delete a forum
     * @param forumId the forum to be deleted id
     * @return true if the forum existed and has been deleted
     */
    boolean deleteForum(String forumId);

    /**
     * Get all threads in the specified forum
     * @param forumId forum id the threads are located in
     * @return list of threads
     */
    List<ForumThreadDTO> listThreads(String forumId);

    /**
     * Get all posts in the specified thread
     * @param forumId forum id the thread is located in
     * @param threadId thread id the posts are written in
     * @return list of posts in the specified thread
     */
    List<ThreadPostDTO> listPosts(String forumId, long threadId);

    /**
     * Get all posts in the specified thread in page
     * @param forumId forum id the thread is located in
     * @param threadId thread id the posts are written in
     * @param startId the id after which the posts are to be selected
     * @param limit maximal amount of posts to select
     * @return
     */
    SeekingPage<ThreadPostDTO> listPostsPaged(String forumId, long threadId, long startId, long limit);

    /**
     * Get a post with the specified id
     * @param forumId forum id the thread is located in
     * @throws net.gastipatis.bignestforum.application.exception.EntityNotFoundException
     * if entity with the specified id in the specified forum does not exist
     */
    ThreadPostDTO getPost(String forumId, long postId);
}
