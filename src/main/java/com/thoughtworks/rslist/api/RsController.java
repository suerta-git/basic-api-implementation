package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thoughtworks.rslist.exception.Error;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {
  @Autowired private UserService userService;
  @Autowired private RsService rsService;

  @GetMapping("/rs/list")
  public List<RsEvent> getRsList(@RequestParam(required = false) Integer start, @RequestParam (required = false) Integer end) {
    if (start == null) {
      start = 1;
    }
    if (end == null) {
      end = rsService.size();
    }
    if (start < 1 || end > rsService.size() || start > end) {
      throw new RsEventNotValidException("invalid request param");
    }
    return rsService.getSubList(start - 1, end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody @Valid RsEvent rsEvent){
    rsService.addRsEvent(rsEvent);
  }

  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index) {
    return rsService.get(index - 1);
  }

  @PatchMapping("/rs/{index}")
  public void updateRsEventOn(@RequestBody RsEvent update, @PathVariable int index) {
    rsService.updateRsEventOn(update, index - 1);
  }

  @DeleteMapping("/rs/{index}")
  public void deleteRsEventOn(@PathVariable int index) {
    rsService.deleteRsEventOn(index - 1);
  }
}
