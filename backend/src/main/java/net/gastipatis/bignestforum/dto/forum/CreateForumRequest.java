package net.gastipatis.bignestforum.dto.forum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CreateForumRequest {

    private String id;
    private long categoryId;
    private String name;
}
