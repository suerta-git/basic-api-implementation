package com.thoughtworks.rslist.po;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "rsEvent")
@ToString
public class RsEventPO {
    @Id @GeneratedValue
    private int id;
    private String eventName;
    private String keyWord;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserPO userPO;

    public RsEventPO(String eventName, String keyWord, UserPO userPO) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userPO = userPO;
    }

    public RsEventPO(RsEventPO rsEventPO) {
        id = rsEventPO.getId();
        eventName = rsEventPO.getEventName();
        keyWord = rsEventPO.getKeyWord();
        userPO = rsEventPO.getUserPO();
    }
}
