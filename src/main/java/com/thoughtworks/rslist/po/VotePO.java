package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vote")
public class VotePO {
    @Id @GeneratedValue
    private int id;

    private Integer voteNum;
    private Integer rsEventId;
    private Integer userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime voteTime;
}
