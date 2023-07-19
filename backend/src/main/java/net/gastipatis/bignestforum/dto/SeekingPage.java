package net.gastipatis.bignestforum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeekingPage<T> {

    private List<T> data;
    private long totalCount;
}
