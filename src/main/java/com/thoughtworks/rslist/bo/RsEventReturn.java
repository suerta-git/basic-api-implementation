package com.thoughtworks.rslist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RsEventReturn {
    private String eventName;
    private String keyWord;
    private int id;
    private int voteNum;
}
