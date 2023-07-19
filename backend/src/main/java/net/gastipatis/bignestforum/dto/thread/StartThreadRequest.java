package net.gastipatis.bignestforum.dto.thread;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
public class StartThreadRequest {

    private String forumId;
    private Optional<String> senderUsername;
    private String topic;
    private String text;
}
