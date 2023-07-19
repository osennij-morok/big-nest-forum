package net.gastipatis.bignestforum.application.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
@Getter
public class CreateRandomThreads {

    private String forumId;
    private int threadsAmount;
    private int postsMin;
    private int postsMax;
}
