package net.gastipatis.bignestforum.application.mapper;

import net.gastipatis.bignestforum.domain.ForumEntity;
import net.gastipatis.bignestforum.dto.forum.ForumDTO;

public interface ForumMapper {

    ForumDTO toDto(ForumEntity entity);

    ForumEntity toEntity(ForumDTO dto);
}
