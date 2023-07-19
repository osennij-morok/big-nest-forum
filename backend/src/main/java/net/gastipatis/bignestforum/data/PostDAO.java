package net.gastipatis.bignestforum.data;

import net.gastipatis.bignestforum.application.exception.EntityNotFoundException;
import net.gastipatis.bignestforum.domain.ForumPostEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostDAO {

    ForumPostEntity newThread(String forumId, long senderId, String topic, String text);

    ForumPostEntity newPost(String forumId, long threadHeadPostId, long senderId, String topic, String text);

    List<ForumPostEntity> getAllThreadPosts(String forumId, Long threadHeadPostId);

    List<ForumPostEntity> getPosts(String forumId, long threadHeadPostId, long fromId, long limit);

    /**
     * @throws EntityNotFoundException if the forum with the specified ID does not exist
     */
    long countPostsInThread(String forumId, long threadHeadPostId) throws EntityNotFoundException;

    List<ForumPostEntity> getAllThreadsInForum(String forumId);

    Optional<ForumPostEntity> getPost(String forumId, long postId);

    boolean delete(String forumId, long postId);

    void deleteAll(String forumId);
}
