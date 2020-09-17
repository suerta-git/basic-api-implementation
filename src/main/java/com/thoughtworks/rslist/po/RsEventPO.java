package com.thoughtworks.rslist.po;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "rsEvent")
public class RsEventPO {
    @Id @GeneratedValue
    private int id;
    private String eventName;
    private String keyWord;
    private Integer userId;

    public RsEventPO(String eventName, String keyWord, Integer userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
    }

    public RsEventPO(RsEventPO rsEventPO) {
        id = rsEventPO.getId();
        eventName = rsEventPO.getEventName();
        keyWord = rsEventPO.getKeyWord();
        userId = rsEventPO.getUserId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RsEventPO rsEventPO = (RsEventPO) o;
        return eventName.equals(rsEventPO.eventName) &&
                keyWord.equals(rsEventPO.keyWord) &&
                userId.equals(rsEventPO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, keyWord, userId);
    }
}
