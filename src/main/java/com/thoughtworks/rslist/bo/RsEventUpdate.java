package com.thoughtworks.rslist.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class RsEventUpdate {
    private String eventName;
    private String keyWord;
    @NotNull
    private Integer userId;
}
