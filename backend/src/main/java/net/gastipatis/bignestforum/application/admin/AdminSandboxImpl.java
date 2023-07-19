package net.gastipatis.bignestforum.application.admin;

import net.datafaker.Faker;
import net.gastipatis.bignestforum.data.PostDAO;
import net.gastipatis.bignestforum.domain.ForumPostEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
public class AdminSandboxImpl implements AdminSandbox {

    private final PostDAO postDAO;

    public AdminSandboxImpl(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    @Override
    public void createRandomThreads(CreateRandomThreads args) {
        var rand = new Random();
        var faker = new Faker(Locale.forLanguageTag("ru"));
        var lorem = faker.lorem();

        for (int threadN = 0; threadN < args.getThreadsAmount(); threadN++) {
            ForumPostEntity thread = postDAO.newThread(args.getForumId(), -1, "", lorem.paragraph());
            int postsAmount = rand.nextInt(args.getPostsMin(), args.getPostsMax());
            for (int postN = 0; postN < postsAmount; postN++) {
                postDAO.newPost(args.getForumId(), thread.getId(), -1, "", lorem.paragraph());
            }
        }
    }

    @Override
    public void clearForum(String forumId) {

    }
}
