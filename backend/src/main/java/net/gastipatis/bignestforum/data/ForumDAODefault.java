package net.gastipatis.bignestforum.data;

import net.gastipatis.bignestforum.data.exception.DeleteEntityException;
import net.gastipatis.bignestforum.data.exception.EntityAlreadyExistsException;
import net.gastipatis.bignestforum.domain.ForumEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.sql.PreparedStatement;
import java.util.List;

@Repository("default")
public class ForumDAODefault implements ForumDAO {

    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;

    public ForumDAODefault(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public ForumEntity get(String id) {
        String sql = "SELECT * FROM forum WHERE id = ?";
        PreparedStatementSetter pss = ps -> {
            ps.setString(1, id);
        };
        ResultSetExtractor<ForumEntity> rse = rs -> {
            if (rs.next()) {
                return new ForumEntity(
                        rs.getString("id"),
                        rs.getLong("category_id"),
                        rs.getString("name"));
            }
            return null;
        };
        ForumEntity forum = jdbcTemplate.query(sql, pss, rse);
        return forum;
    }

    @Override
    public List<ForumEntity> get() {
        String sql = "SELECT * FROM forum";
        List<ForumEntity> forums = jdbcTemplate.query(sql, (rs, rowNum) -> new ForumEntity(
                rs.getString("id"),
                rs.getLong("category_id"),
                rs.getString("name")));
        return forums;
    }

    @Override
    public void createForum(String id, long categoryId, String name) {
        TransactionStatus transaction = transactionManager.getTransaction(null);
        try {
            String createForumSql = "INSERT INTO forum (id, category_id, name) VALUES (?, ?, ?)";
            boolean forumInserted = jdbcTemplate.update(createForumSql, id, categoryId, name) == 1;
            String postsTableName = "post_" + id;
            String createPostsTableSql = "CREATE TABLE IF NOT EXISTS " + postsTableName + " (" +
                    "id BIGSERIAL PRIMARY KEY," +
                    "parent_id BIGINT DEFAULT NULL REFERENCES " + postsTableName + "(id) ON DELETE CASCADE," +
                    "sender_id BIGINT DEFAULT NULL REFERENCES account(id)," +
                    "publish_time TIMESTAMP NOT NULL DEFAULT NOW()," +
                    "topic TEXT NOT NULL DEFAULT ''," +
                    "text TEXT NOT NULL)";
            jdbcTemplate.execute(createPostsTableSql);
            transactionManager.commit(transaction);
        } catch (DuplicateKeyException ex) {
            transactionManager.rollback(transaction);
            throw new EntityAlreadyExistsException("This forum already exists", ex);
        } catch (DataAccessException ex) {
            transactionManager.rollback(transaction);
            throw ex;
        }
    }

    @Override
    public ForumEntity update(ForumEntity entity) {
        return null;
    }

    @Override
    public boolean delete(String id) throws DeleteEntityException {
        TransactionStatus transaction = transactionManager.getTransaction(null);
        try {
            String deleteForumSql = "DELETE FROM forum WHERE id = ?";
            PreparedStatementCreator psCreator = con -> {
                PreparedStatement ps = con.prepareStatement(deleteForumSql);
                ps.setString(1, id);
                return ps;
            };
            boolean existedAndHasBeenDeleted = jdbcTemplate.update(psCreator) >= 1;

            String deletePostsTableSql = "DROP TABLE IF EXISTS " + "post_" + id + ";";
            jdbcTemplate.execute(deletePostsTableSql);

            transactionManager.commit(transaction);
            return existedAndHasBeenDeleted;
        } catch (DataAccessException ex) {
            transactionManager.rollback(transaction);
            throw ex;
        }
    }
}
