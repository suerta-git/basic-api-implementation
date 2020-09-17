package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RsService {
    private final List<RsEvent> rsList = new ArrayList<>();

    @Autowired private UserService userService;
    @Autowired private RsRepository rsRepository;
    @Autowired private UserRepository userRepository;

    public int size() {
        return (int) rsRepository.count();
    }

    public List<RsEvent> getSubList(int start, int end) {
        return rsRepository.findAll()
                .subList(start, end)
                .stream()
                .map(this::toRsEvent)
                .collect(Collectors.toList());
    }

    public int addRsEvent(RsEvent rsEvent) {
        final int userId = rsEvent.getUserId();
        if (!userService.isExistById(userId)) {
            throw new RsEventNotValidException("invalid param");
        }
        if (isContainsRsEvent(rsEvent)) {
            throw new RsEventNotValidException("event exists");
        }
        RsEventPO rsEventPO = toRsEventPO(rsEvent);
        rsRepository.save(rsEventPO);
        return rsEventPO.getId();
    }

    private boolean isContainsRsEvent(RsEvent rsEvent) {
        return rsRepository.findAll().contains(toRsEventPO(rsEvent));
    }

    public RsEvent get(int eventId) {
        return toRsEvent(rsRepository
                .findById(eventId)
                .orElseThrow(() -> new RsEventNotValidException("invalid event id")));
    }

    public void updateRsEventOn(RsEvent update, int eventId) {
        RsEventPO rsEventPO = rsRepository.findById(eventId)
                .orElseThrow(() -> new RsEventNotValidException("invalid event id"));

        RsEventPO newRsEventPO = new RsEventPO(rsEventPO);
        if (update.getEventName() != null) {
            newRsEventPO.setEventName(update.getEventName());
        }
        if (update.getKeyWord() != null) {
            newRsEventPO.setKeyWord(update.getKeyWord());
        }
        if (update.getUserId() != null) {
            final int userId = update.getUserId();
            if (!userService.isExistById(userId)) {
                throw new RsEventNotValidException("invalid param");
            }
            newRsEventPO.setUserId(userId);
        }
        if (isContainsRsEvent(toRsEvent(newRsEventPO))) {
            throw new RsEventNotValidException("event exists");
        }
        rsRepository.save(newRsEventPO);
    }

    public void deleteRsEventOn(int index) {
        rsList.remove(index);
    }

    public void init(List<RsEvent> rsEvents) {
        rsList.clear();
        rsList.addAll(rsEvents);
    }

    private RsEventPO toRsEventPO(RsEvent rsEvent) {
        return RsEventPO.builder()
                .eventName(rsEvent.getEventName())
                .keyWord(rsEvent.getKeyWord())
                .userId(rsEvent.getUserId())
                .build();
    }

    private RsEvent toRsEvent(RsEventPO rsEventPO) {
        return new RsEvent(rsEventPO.getEventName(), rsEventPO.getKeyWord(), rsEventPO.getUserId());
    }
}
