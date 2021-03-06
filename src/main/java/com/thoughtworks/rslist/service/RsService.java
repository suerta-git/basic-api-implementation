package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.bo.RsEvent;
import com.thoughtworks.rslist.bo.RsEventReturn;
import com.thoughtworks.rslist.bo.RsEventUpdate;
import com.thoughtworks.rslist.bo.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RsService {
    @Autowired private UserService userService;
    @Autowired private RsRepository rsRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private VoteRepository voteRepository;

    public int size() {
        return (int) rsRepository.count();
    }

    public List<RsEventReturn> getSubList(int start, int end) {
        return rsRepository.findAll()
                .subList(start, end)
                .stream()
                .map(this::toRsEventReturn)
                .collect(Collectors.toList());
    }

    public int addRsEvent(RsEvent rsEvent) {
        final int userId = rsEvent.getUserId();
        if (!userService.isExistById(userId)) {
            throw new RsEventNotValidException("invalid param");
        }
        RsEventPO rsEventPO = toRsEventPO(rsEvent);
        rsRepository.save(rsEventPO);
        return rsEventPO.getId();
    }

    public RsEventReturn get(int eventId) {
        return toRsEventReturn(rsRepository
                .findById(eventId)
                .orElseThrow(() -> new RsEventNotValidException("invalid event id")));
    }

    public void updateRsEventOn(RsEventUpdate update, int eventId) {
        RsEventPO rsEventPO = rsRepository.findById(eventId)
                .orElseThrow(() -> new RsEventNotValidException("invalid event id"));

        if (update.getUserId() != rsEventPO.getUserPO().getId()) {
            throw new RsEventNotValidException("invalid user id");
        }
        if (update.getEventName() != null) {
            rsEventPO.setEventName(update.getEventName());
        }
        if (update.getKeyWord() != null) {
            rsEventPO.setKeyWord(update.getKeyWord());
        }
        rsRepository.save(rsEventPO);
    }

    public void deleteRsEvent(int eventId) {
        if (!rsRepository.existsById(eventId)) {
            throw new RsEventNotValidException("invalid event id");
        }
        rsRepository.deleteById(eventId);
    }

    private RsEventPO toRsEventPO(RsEvent rsEvent) {
        return RsEventPO.builder()
                .eventName(rsEvent.getEventName())
                .keyWord(rsEvent.getKeyWord())
                .userPO(userRepository.findById(rsEvent.getUserId()).orElse(null))
                .build();
    }

    private RsEventReturn toRsEventReturn(RsEventPO rsEventPO) {
        return new RsEventReturn(rsEventPO.getEventName(), rsEventPO.getKeyWord(), rsEventPO.getId(), rsEventPO.getVoteNum());
    }

    public void voteTo(Vote vote, int eventId) {
        RsEventPO rsEventPO = rsRepository.findById(eventId).orElseThrow(() -> new RsEventNotValidException("invalid event id"));
        UserPO userPO = userRepository.findById(vote.getUserId()).orElseThrow(() -> new RsEventNotValidException("invalid user id"));
        if (vote.getVoteNum() > userPO.getVoteNum()) {
            throw new RsEventNotValidException("user's vote number not enough");
        }
        userPO.vote(vote.getVoteNum());
        rsEventPO.setVoteNum(rsEventPO.getVoteNum() + vote.getVoteNum());
        userRepository.save(userPO);
        rsRepository.save(rsEventPO);

        VotePO votePO = VotePO.builder()
                .voteNum(vote.getVoteNum())
                .rsEventId(rsEventPO.getId())
                .userId(userPO.getId())
                .voteTime(vote.getVoteTime()).build();
        voteRepository.save(votePO);
    }
}
