package net.gastipatis.bignestforum.data;

import jakarta.persistence.EntityManager;
import net.gastipatis.bignestforum.data.exception.DeleteEntityException;
import net.gastipatis.bignestforum.domain.ForumEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jpa")
public class ForumDAOWithJPA implements ForumDAO {

    private final EntityManager entityManager;

    public ForumDAOWithJPA(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ForumEntity get(String id) {

        return null;
    }

    @Override
    public List<ForumEntity> get() {
        return null;
    }

    @Override
    public void createForum(String id, long categoryId, String name) {

    }

    @Override
    public ForumEntity update(ForumEntity entity) {
        return null;
    }

    @Override
    public boolean delete(String id) throws DeleteEntityException {
        return false;
    }
}
