package net.gastipatis.bignestforum.application.mapper;

import net.gastipatis.bignestforum.domain.ForumPostEntity;
import net.gastipatis.bignestforum.dto.thread.ForumThreadDTO;
import org.springframework.stereotype.Component;

@Component
public class ThreadMapperImpl implements ThreadMapper {

    @Override
    public ForumThreadDTO toDto(ForumPostEntity entity) {
        return ForumThreadDTO.builder()
                .id(entity.getId())
                .forumId(entity.getForumId())
//                .senderId(entity.getSenderId())
                .sender(entity.getSender())
                .topic(entity.getTopic())
                .text(entity.getText())
                .build();
    }

    @Override
    public ForumPostEntity toEntity(ForumThreadDTO dto) {
        return ForumPostEntity.builder()
                .id(dto.getId())
                .parentId(0L)
//                .senderId(dto.getSenderId())
                .sender(dto.getSender())
                .forumId(dto.getForumId())
                .topic(dto.getTopic())
                .text(dto.getText())
                .build();
    }
}
