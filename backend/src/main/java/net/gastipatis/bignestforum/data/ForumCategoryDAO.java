package net.gastipatis.bignestforum.data;

import net.gastipatis.bignestforum.domain.ForumCategory;
import org.springframework.data.repository.CrudRepository;

public interface ForumCategoryDAO extends CrudRepository<ForumCategory, Long> {

}
