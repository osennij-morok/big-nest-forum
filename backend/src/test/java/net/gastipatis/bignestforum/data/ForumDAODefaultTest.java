package net.gastipatis.bignestforum.data;

import net.gastipatis.bignestforum.domain.ForumEntity;
import org.assertj.db.type.Request;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Tag("unit")
class ForumDAODefaultTest {

    @Autowired
    @Qualifier("default")
    private ForumDAODefault dao;
    @Autowired
    private DataSource dataSource;
//    private Table forumTable;

//    @BeforeEach
//    void init() {
//        forumTable = new Table(dataSource, "forum");
//    }

    private Predicate<ForumEntity> forumHasFieldsPredicate(String id, String name) {
        return forum -> forum.getId().equals(id) && forum.getName().equals(name);
    }

    @Test
    @Sql({"/db/init-data-for-getForum.sql"})
    void should_get_all_forums() {
        List<ForumEntity> forums = dao.get();
        assertThat(forums)
                .anyMatch(forumHasFieldsPredicate("f", "Flooding"))
                .anyMatch(forumHasFieldsPredicate("vg", "Video-games"));
    }

    @Test
    @Sql({"/db/init-data-for-getForum.sql"})
    void getOneForum_then_found() {
        ForumEntity forum = dao.get("f");
        assertThat(forum).isNotNull();
    }

    @Test
    @Sql({"/db/init-data-for-createForum.sql"})
    void createForum_then_success() {
        String forumId = "f";
        dao.createForum(forumId, 1L, "Flooding");
        try {
            dao.createForum(forumId, 1L, "Flooding");
        } catch (Exception e) {
            System.out.println();
        }

        var forumRequest = new Request(dataSource, "SELECT * FROM forum WHERE id = ?", forumId);
        org.assertj.db.api.Assertions.assertThat(forumRequest)
                .as("One forum can be created")
                .hasNumberOfRows(1)
                .column("id").value().isEqualTo(forumId)
                .column("name").value().isEqualTo("Flooding");

        Table postsTable = new Table(dataSource, "post_" + forumId);
        org.assertj.db.api.Assertions.assertThat(postsTable)
                .exists();
    }

    @Test
    @Sql({"/db/init-data-for-deleteForum.sql"})
    void when_forumAndPostsTableAreCreated_thenIf_deleteForum_then_success() {
        String forumId = "f";

        dao.delete(forumId);

        var forumRequest = new Request(dataSource, "SELECT * FROM forum WHERE id = ?", forumId);
        org.assertj.db.api.Assertions.assertThat(forumRequest)
                .isEmpty();

        var postsTable = new Table(dataSource, "post_" + forumId);
        org.assertj.db.api.Assertions.assertThat(postsTable)
                .doesNotExist();
    }
}