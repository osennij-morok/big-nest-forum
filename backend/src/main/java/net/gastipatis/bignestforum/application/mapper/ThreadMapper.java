package net.gastipatis.bignestforum.application.mapper;

import net.gastipatis.bignestforum.domain.ForumPostEntity;
import net.gastipatis.bignestforum.dto.thread.ForumThreadDTO;

public interface ThreadMapper {

    ForumThreadDTO toDto(ForumPostEntity entity);

    ForumPostEntity toEntity(ForumThreadDTO dto);
}
