package net.gastipatis.bignestforum.dto.forum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ForumCategoryDTO {

    private long id;

    @NotNull
    @NotBlank
    private String name;
}
