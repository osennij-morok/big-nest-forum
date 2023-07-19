package net.gastipatis.bignestforum.dto.thread;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.gastipatis.bignestforum.domain.PostSender;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumThreadDTO {

    private long id;
//    private long senderId;
    private PostSender sender;
    private String forumId;
    private String topic;
    private String text;
}
