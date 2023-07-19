package net.gastipatis.bignestforum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class PostInThreadRequest {

    private String forumId;
    private Optional<String> senderUsername;
    private long threadHeadPostId;
    private String topic;
    private String text;
}
