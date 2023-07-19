package net.gastipatis.bignestforum.data;

import net.gastipatis.bignestforum.data.exception.DeleteEntityException;
import net.gastipatis.bignestforum.domain.ForumCategory;
import net.gastipatis.bignestforum.domain.ForumEntity;

import java.util.List;

public interface ForumDAO {

    ForumEntity get(String id);

    List<ForumEntity> get();

    void createForum(String id, long categoryId, String name);

    ForumEntity update(ForumEntity entity);

    boolean delete(String id) throws DeleteEntityException;
}
