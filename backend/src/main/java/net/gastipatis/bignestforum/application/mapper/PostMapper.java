package net.gastipatis.bignestforum.application.mapper;

import net.gastipatis.bignestforum.domain.ForumPostEntity;
import net.gastipatis.bignestforum.dto.post.ThreadPostDTO;

public interface PostMapper {

    ThreadPostDTO toDto(ForumPostEntity entity);

    ForumPostEntity toEntity(ThreadPostDTO dto, String forumId);
}
