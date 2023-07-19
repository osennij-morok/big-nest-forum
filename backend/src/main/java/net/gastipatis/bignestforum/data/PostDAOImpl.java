package net.gastipatis.bignestforum.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.gastipatis.bignestforum.application.exception.EntityNotFoundException;
import net.gastipatis.bignestforum.domain.ForumPostEntity;
import net.gastipatis.bignestforum.domain.PostSender;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDAOImpl implements PostDAO {

    public static final String POST_TABLE_PREFIX = "post_";
    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    public PostDAOImpl(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
    }

    private PostSender selectPostSender(long id) {
        return entityManager.find(PostSender.class, id);
    }

    private ForumPostEntity insertPost(String forumId, long parentId, long senderId, String topic, String text) {
        TransactionStatus transaction = transactionManager.getTransaction(null);
        try {
            var insertOperation = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(POST_TABLE_PREFIX + forumId)
                    .usingColumns("topic", "text", "parent_id", "sender_id")
                    .usingGeneratedKeyColumns("id", "publish_time")
                    .withoutTableColumnMetaDataAccess();
            System.out.println("Parent id before insert: " + parentId); ////
            var params = new MapSqlParameterSource()
                .addValue("topic", topic)
                .addValue("text", text)
                .addValue("parent_id", parentId == -1 ? null : parentId)
                .addValue("sender_id", senderId == -1 ? null : senderId);
            KeyHolder keyHolder = insertOperation.executeAndReturnKeyHolder(params);
            PostSender sender = senderId == -1 ? null : selectPostSender(senderId);
            transactionManager.commit(transaction);
            var generatedId = (Long) keyHolder.getKeys().get("id");
            var publishDate = (Date) keyHolder.getKeys().get("publish_time");
            LocalDateTime publishTime = new Timestamp(publishDate.getTime()).toLocalDateTime();
            return ForumPostEntity.builder()
                    .id(generatedId)
                    .parentId(parentId)
//                    .senderId(senderId)
                    .sender(sender)
                    .forumId(forumId)
                    .topic(topic)
                    .text(text)
                    .publishTime(publishTime)
                    .build();
        } catch (DataAccessException ex) {
            transactionManager.rollback(transaction);
            throw ex;
        }
    }

    // TODO: TEST IT
    @Override
    public ForumPostEntity newThread(String forumId, long senderId, String topic, String text) {
        return insertPost(forumId, -1, senderId, topic, text);
    }

    @Override
    public ForumPostEntity newPost(String forumId, long threadHeadPostId, long senderId, String topic, String text) {
        System.out.println("Inserting post with head post id: " + threadHeadPostId); ////
        return insertPost(forumId, threadHeadPostId, senderId, topic, text);
    }

    private List<ForumPostEntity> getAllPosts(String forumId, Long parentId) {
        TransactionStatus transaction = transactionManager.getTransaction(null);
        try {
            String postsSql =
                    "SELECT post.id AS id, post.parent_id AS parent_id, " +
                           "post.topic AS topic, post.text AS text, " +
                           "post.sender_id AS sender_id, account.username AS sender_username, " +
                           "post.publish_time AS publish_time " +
                    "FROM " + POST_TABLE_PREFIX + forumId + " AS post " +
                    "LEFT JOIN account ON post.sender_id = account.id ";
            String whereClause = "WHERE post.parent_id ";
            boolean queryingForThread = parentId == null;
            if (queryingForThread) {
                whereClause += "IS NULL";
            } else {
                whereClause += "= ? ORDER BY post.id";
            }
            postsSql += whereClause;
            RowMapper<ForumPostEntity> postRowMapper = (rs, rowNum) -> ForumPostEntity.builder()
                    .forumId(forumId)
                    .id(rs.getLong("id"))
                    .parentId(parentId)
//                    .senderId(rs.getLong("sender_id"))
                    .sender(rs.getLong("sender_id") == 0
                            ? null
                            : PostSender.builder()
                            .id(rs.getLong("sender_id"))
                            .username(rs.getString("sender_username"))
                            .build())
                    .topic(rs.getString("topic"))
                    .publishTime(rs.getTimestamp("publish_time").toLocalDateTime())
                    .text(rs.getString("text"))
                    .build();
            List<ForumPostEntity> posts;
            if (queryingForThread) {
                posts = jdbcTemplate.query(postsSql, postRowMapper);
            } else {
                PreparedStatementSetter psSetter = ps -> ps.setObject(1, parentId);
                posts = jdbcTemplate.query(postsSql, psSetter, postRowMapper);
            }
            transactionManager.commit(transaction);
            return posts;
        } catch (Exception ex) {
            transactionManager.rollback(transaction);
            throw ex;
        }
    }

    @Override
    public List<ForumPostEntity> getAllThreadPosts(String forumId, Long threadHeadPostId) {
        return getAllPosts(forumId, threadHeadPostId);
    }

    @Override
    public List<ForumPostEntity> getPosts(String forumId, long threadHeadPostId, long fromId, long limit) {
        String postTableName = POST_TABLE_PREFIX + forumId;
        // todo: SQL INJECTION ALERT!!!
        String query =
                "SELECT post.id AS id, " +
                       "post.parent_id AS parent_id, " +
                       "post.sender_id AS sender_id, " +
                       "post.publish_time AS publish_time, " +
                       "post.topic AS topic, " +
                       "post.text AS text, " +
//                       "account.id, account.username, " +
                       ":forumId AS forum_id " +
                "FROM " + postTableName + " AS post " +
                "LEFT JOIN account ON account.id = post.sender_id " +
                "WHERE post.parent_id = :parentId AND post.id > :fromId " +
                "ORDER BY post.id " +
                "LIMIT :lim ";
        Session session = entityManager.unwrap(Session.class);
        return session.createNativeQuery(query, ForumPostEntity.class)
                .setParameter("forumId", forumId)
                .setParameter("parentId", threadHeadPostId)
                .setParameter("fromId", fromId)
                .setParameter("lim", limit)
                .getResultList();
    }

    @Override
    public long countPostsInThread(String forumId, long threadHeadPostId) throws EntityNotFoundException {
        String postTableName = POST_TABLE_PREFIX + forumId;
        String query = "SELECT count(*) FROM " + postTableName + " WHERE parent_id = :headPostId";
        Session session = entityManager.unwrap(Session.class);
        return (Long) session.createNativeQuery(query)
                .setParameter("headPostId", threadHeadPostId)
                .getSingleResult();
    }

    @Override
    public List<ForumPostEntity> getAllThreadsInForum(String forumId) {
        return getAllPosts(forumId, null);
    }

    @Override
    public Optional<ForumPostEntity> getPost(String forumId, long postId) {
        TransactionStatus transaction = transactionManager.getTransaction(null);
        try {
            // TODO: EXTRACT IT
            RowMapper<ForumPostEntity> postRowMapper = (rs, rowNum) -> ForumPostEntity.builder()
                    .forumId(forumId)
                    .id(rs.getLong("id"))
                    .parentId(rs.getLong("parent_id"))
//                    .senderId(rs.getLong("sender_id"))
                    .sender(rs.getLong("sender_id") == 0
                            ? null
                            : PostSender.builder()
                            .id(rs.getLong("sender_id"))
                            .username(rs.getString("sender_username"))
                            .build())
                    .topic(rs.getString("topic"))
                    .publishTime(rs.getTimestamp("publish_time").toLocalDateTime())
                    .text(rs.getString("text"))
                    .build();
            ResultSetExtractor<ForumPostEntity> resultSetExtractor = rs -> {
                if (rs.next()) {
                    return postRowMapper.mapRow(rs, 0);
                }
                return null;
            };
            String postsTable = "post_" + forumId;
            String postSql =
                    "SELECT post.id AS id, post.sender_id AS sender_id, " +
                           "post.parent_id AS parent_id, post.topic AS topic, " +
                           "post.text AS text, account.username AS sender_username, " +
                           "post.publish_time AS publish_time " +
                    "FROM " + postsTable + " AS post " +
                    "LEFT JOIN account ON account.id = post.sender_id " +
                    "WHERE post.id = ? ";
            ForumPostEntity post = jdbcTemplate.query(
                    postSql,
                    ps -> ps.setLong(1, postId),
                    resultSetExtractor);
            transactionManager.commit(transaction);
            return Optional.ofNullable(post);
        } catch (Exception ex) {
            transactionManager.rollback(transaction);
            throw ex;
        }
    }

    @Override
    public boolean delete(String forumId, long postId) {
        TransactionStatus transaction = transactionManager.getTransaction(null);
        try {
            String postTableName = POST_TABLE_PREFIX + forumId;
            String deleteSql = "DELETE FROM " + postTableName + " WHERE id = ?";
            PreparedStatementCreator psc = con -> {
                PreparedStatement ps = con.prepareStatement(deleteSql);
                ps.setLong(1, postId);
                return ps;
            };
            boolean successfullyDeleted = jdbcTemplate.update(psc) >= 1;
            transactionManager.commit(transaction);
            return successfullyDeleted;
        } catch (DataAccessException ex) {
            transactionManager.rollback(transaction);
            throw ex;
        }
    }

    @Override
    public void deleteAll(String forumId) {
        String postTableName = POST_TABLE_PREFIX + forumId;
        entityManager.createQuery("delete from " + postTableName).executeUpdate();
    }
}
