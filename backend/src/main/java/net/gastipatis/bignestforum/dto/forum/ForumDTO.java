package net.gastipatis.bignestforum.dto.forum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumDTO {

    @NotNull
    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    private long categoryId;

    @NotNull
    @NotBlank
    private String name;
}
