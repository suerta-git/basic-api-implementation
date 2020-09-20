package com.thoughtworks.rslist.bo;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Builder
@EqualsAndHashCode
public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @NotNull
    private Integer userId;

    public RsEvent(String eventName, String keyWord, int userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
    }

    public RsEvent() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
