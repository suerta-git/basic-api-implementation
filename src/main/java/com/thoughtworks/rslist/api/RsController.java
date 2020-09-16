package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  public static final User DEFAULT_USER = new User("test", 20, "male", "test@test.com", "10987654321");
  private final List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
          new RsEvent("第一条事件", "无标签", DEFAULT_USER),
          new RsEvent("第二条事件", "无标签", DEFAULT_USER),
          new RsEvent("第三条事件", "无标签", DEFAULT_USER)));

  @Autowired
  private UserService userService;

  @GetMapping("/rs/list")
  public List<RsEvent> getRsList(@RequestParam(required = false) Integer start, @RequestParam (required = false) Integer end) {
    if (start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody @Valid RsEvent rsEvent){
    rsList.add(rsEvent);
    userService.addUser(rsEvent.getUser());
  }

  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index) {
    return rsList.get(index - 1);
  }

  @PatchMapping("/rs/{index}")
  public void updateOneRsEvent(@PathVariable int index, @RequestBody RsEvent update) {
    if (update.getEventName() != null) {
      rsList.get(index - 1).setEventName(update.getEventName());
    }
    if (update.getKeyWord() != null) {
      rsList.get(index - 1).setKeyWord(update.getKeyWord());
    }
    if (update.getUser() != null) {
      rsList.get(index - 1).setUser(update.getUser());
    }
  }

  @DeleteMapping("/rs/{index}")
  public void deleteOneRsEvent(@PathVariable int index) {
    rsList.remove(index - 1);
  }
}
