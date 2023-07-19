package net.gastipatis.bignestforum.application.admin;

public interface AdminSandbox {

    void createRandomThreads(CreateRandomThreads args);

    void clearForum(String forumId);
}
