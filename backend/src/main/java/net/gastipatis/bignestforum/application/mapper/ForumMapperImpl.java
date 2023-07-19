package net.gastipatis.bignestforum.application.mapper;

import net.gastipatis.bignestforum.domain.ForumEntity;
import net.gastipatis.bignestforum.dto.forum.ForumDTO;
import org.springframework.stereotype.Component;

@Component
public class ForumMapperImpl implements ForumMapper {

    @Override
    public ForumDTO toDto(ForumEntity entity) {
        return ForumDTO.builder()
                .id(entity.getId())
                .categoryId(entity.getCategoryId())
                .name(entity.getName())
                .build();
    }

    @Override
    public ForumEntity toEntity(ForumDTO dto) {
        return ForumEntity.builder()
                .id(dto.getId())
                .categoryId(dto.getCategoryId())
                .name(dto.getName())
                .build();
    }
}
