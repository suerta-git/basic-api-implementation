package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RsService {
    private final List<RsEvent> rsList = new ArrayList<>();

    @Autowired private UserService userService;

    public List<RsEvent> getList() {
        return rsList;
    }

    public List<RsEvent> getSubList(int start, int end) {
        return rsList.subList(start, end);
    }

    public void addRsEvent(RsEvent rsEvent) {
        final String userName = rsEvent.getUser().getUserName();
        if (!userService.isExistByName(userName)) {
            userService.addUser(rsEvent.getUser());
        } else {
            rsEvent.setUser(userService.getUser(userName));
        }
        rsList.add(rsEvent);
    }

    public RsEvent get(int index) {
        return rsList.get(index);
    }

    public void updateRsEventOn(RsEvent update, int index) {
        if (update.getEventName() != null) {
            rsList.get(index).setEventName(update.getEventName());
        }
        if (update.getKeyWord() != null) {
            rsList.get(index).setKeyWord(update.getKeyWord());
        }
        if (update.getUser() != null) {
            final String userName = update.getUser().getUserName();
            User correctUser = update.getUser();

            if (!userService.isExistByName(userName)) {
                userService.addUser(correctUser);
            } else {
                correctUser = userService.getUser(userName);
            }
            rsList.get(index).setUser(correctUser);
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
