package net.gastipatis.bignestforum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
@Builder
public class PostInThreadRequestBody {

    private long threadHeadPostId;
    private String topic;
    private String text;

    public PostInThreadRequest intoRequest(String forumId, Optional<String> senderUsername) {
        return PostInThreadRequest.builder()
                .forumId(forumId)
                .threadHeadPostId(threadHeadPostId)
                .topic(topic)
                .text(text)
                .senderUsername(senderUsername)
                .build();
    }
}
