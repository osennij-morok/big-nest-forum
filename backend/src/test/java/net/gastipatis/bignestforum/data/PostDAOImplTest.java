package net.gastipatis.bignestforum.data;

import net.datafaker.Faker;
import net.gastipatis.bignestforum.domain.ForumPostEntity;
import org.assertj.db.type.Request;
import org.jooq.*;
import org.jooq.Record;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.jooq.impl.DSL.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("unit")
class PostDAOImplTest {

    @Autowired
    private PostDAOImpl postDAO;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DSLContext dsl;

    @Test
    @Sql({"/db/init-data-for-newThread.sql"})
    void when_newThread_then_success() {
        String forumId = "f";
        String topic = "My greetings";
        String text = "Hello!";
        ForumPostEntity post = postDAO.newThread(forumId, -1, topic, text);

        assertThat(post.getId()).isNotZero();
        assertThat(post.getForumId()).isEqualTo(forumId);
        assertThat(post.getTopic()).isEqualTo(topic);
        assertThat(post.getText()).isEqualTo(text);

        var postRequest = new Request(dataSource, "SELECT * FROM " + "post_" + forumId);
        org.assertj.db.api.Assertions.assertThat(postRequest)
                .hasNumberOfRows(1)
                .column("topic").value().isEqualTo(topic)
                .column("text").value().isEqualTo(text);
    }

    final Field<Object> TOPIC = field("topic");
    final Field<Object> TEXT = field("text");
    final Field<Object> PARENT_ID = field("parent_id");
    final Field<Object> SENDER_ID = field("sender_id");

    private Collection<Record2<Object, Object>> randomThreadsRecords() {
        List<String> paragraphs = new Faker(Locale.forLanguageTag("ru"))
                .lorem()
                .paragraphs(50);
        Collection<Record2<Object, Object>> paragraphsRecords = paragraphs.stream()
                .map(p -> dsl.newRecord(TOPIC, TEXT)
                        .with(TOPIC, "")
                        .with(TEXT, p))
                .collect(Collectors.toList());
        return paragraphsRecords;
    }

    private Collection<Record4<Object, Object, Object, Object>> randomPostsRecords(Long threadId) {
        List<String> paragraphs = new Faker(Locale.forLanguageTag("ru"))
                .lorem()
                .paragraphs(50);
        Collection<Record4<Object, Object, Object, Object>> paragraphsRecords = paragraphs.stream()
                .map(p -> dsl.newRecord(TOPIC, TEXT, PARENT_ID, SENDER_ID)
                        .with(TOPIC, "")
                        .with(TEXT, p)
                        .with(PARENT_ID, threadId)
                        .with(SENDER_ID, 1L))
                .collect(Collectors.toList());
        return paragraphsRecords;
    }

    @Test
    @Sql({"/db/init-data-for-sample1.sql"})
    void given_someThreadsAndPosts_do_getPosts_expect_success() {
        Field<Object> ACCOUNT_USERNAME = field("username");
        Field<Object> ACCOUNT_PASSWORD_HASH = field("password_hash");

        int accountsInserted = dsl
                .insertInto(table("account"))
                .columns(ACCOUNT_USERNAME, ACCOUNT_PASSWORD_HASH)
                .valuesOfRecords(dsl.newRecord(ACCOUNT_USERNAME, ACCOUNT_PASSWORD_HASH)
                        .with(ACCOUNT_USERNAME, "general")
                        .with(ACCOUNT_PASSWORD_HASH, "1234"))
                .execute();

        int threadsInserted = dsl
                .insertInto(table("post_f"))
                .columns(TOPIC, TEXT)
                .valuesOfRecords(randomThreadsRecords())
                .execute();

        int postsInserted = dsl
                .insertInto(table("post_f"))
                .columns(TOPIC, TEXT, PARENT_ID, SENDER_ID)
                .valuesOfRecords(randomPostsRecords(1L))
                .execute();


//        Result<Record> result = dsl
//                .select()
//                .from(table("post_f"))
//                .orderBy(field("id"))
//                .seek(10)
//                .limit(10)
//                .fetch();

        List<ForumPostEntity> threads = postDAO.getAllThreadsInForum("f");

        List<ForumPostEntity> posts = postDAO.getPosts("f", 1L, 70L, 20L);

        assertThat(posts).isNotEmpty();
    }
}