package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RsService {
    private final List<RsEvent> rsList = new ArrayList<>();

    @Autowired private UserService userService;

    public int size() {
        return rsList.size();
    }

    public List<RsEvent> getList() {
        return rsList;
    }

    public List<RsEvent> getSubList(int start, int end) {
        return rsList.subList(start, end);
    }

    public int addRsEvent(RsEvent rsEvent) {
        final int userId = rsEvent.getUserId();
        if (!userService.isExistById(userId)) {
            throw new RsEventNotValidException("invalid param");
        }
        rsList.add(rsEvent);
        return rsList.size() - 1;
    }

    public RsEvent get(int index) {
        if (index < 0 || index >= size()) {
            throw new RsEventNotValidException("invalid index");
        }
        return rsList.get(index);
    }

    public void updateRsEventOn(RsEvent update, int index) {
        if (update.getEventName() != null) {
            rsList.get(index).setEventName(update.getEventName());
        }
        if (update.getKeyWord() != null) {
            rsList.get(index).setKeyWord(update.getKeyWord());
        }
        if (update.getUserId() != null) {
            final int userId = update.getUserId();
            if (!userService.isExistById(userId)) {
                throw new RsEventNotValidException("invalid param");
            }
            rsList.get(index).setUserId(userId);
        }
    }

    public void deleteRsEventOn(int index) {
        rsList.remove(index);
    }

    public void init(List<RsEvent> rsEvents) {
        rsList.clear();
        rsList.addAll(rsEvents);
    }
}
