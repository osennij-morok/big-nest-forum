package net.gastipatis.bignestforum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.gastipatis.bignestforum.domain.PostSender;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThreadPostDTO {

    private long id;
    private PostSender sender;
    private long threadHeadPostId;
    private String topic;
    private String text;
    private long publishTimestamp;
}
