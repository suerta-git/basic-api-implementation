package com.thoughtworks.rslist.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class Vote {
    @NotNull
    private int voteNum;
    @NotNull
    private int userId;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime voteTime;
}
