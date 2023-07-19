package net.gastipatis.bignestforum.dto.thread;

import lombok.*;

import java.util.Optional;

@AllArgsConstructor
@Getter
@Builder
public class StartThreadRequestBody {

    private String topic;
    private String text;

    public StartThreadRequest intoRequest(String forumId, Optional<String> senderUsername) {
        return StartThreadRequest.builder()
                .senderUsername(senderUsername)
                .topic(topic)
                .text(text)
                .forumId(forumId)
                .build();
    }
}
