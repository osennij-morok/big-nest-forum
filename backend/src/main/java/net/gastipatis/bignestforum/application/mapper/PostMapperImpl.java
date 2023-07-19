package net.gastipatis.bignestforum.application.mapper;

import net.gastipatis.bignestforum.domain.ForumPostEntity;
import net.gastipatis.bignestforum.dto.post.ThreadPostDTO;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public ThreadPostDTO toDto(ForumPostEntity entity) {
        long publishTimestamp = -1L;
        if (entity.getPublishTime() != null) {
            publishTimestamp = entity.getPublishTime().toEpochSecond(ZoneOffset.UTC);
        }
        return ThreadPostDTO.builder()
                .id(entity.getId())
//                .senderId(entity.getSenderId() == null || entity.getSenderId() == 0L
//                        ? -1L
//                        : entity.getSenderId())
                .sender(entity.getSender())
                .threadHeadPostId(entity.getParentId())
                .topic(entity.getTopic())
                .text(entity.getText())
                .publishTimestamp(publishTimestamp)
                .build();
    }

    @Override
    public ForumPostEntity toEntity(ThreadPostDTO dto, String forumId) {
        return ForumPostEntity.builder()
                .id(dto.getId())
//                .senderId(dto.getSenderId())
                .sender(dto.getSender())
                .parentId(dto.getThreadHeadPostId())
                .forumId(forumId)
                .topic(dto.getTopic())
                .text(dto.getText())
                .build();
    }
}
