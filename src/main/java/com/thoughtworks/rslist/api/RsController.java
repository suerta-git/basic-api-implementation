package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.bo.RsEvent;
import com.thoughtworks.rslist.bo.RsEventReturn;
import com.thoughtworks.rslist.bo.RsEventUpdate;
import com.thoughtworks.rslist.bo.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {
  @Autowired private UserService userService;
  @Autowired private RsService rsService;

  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEventReturn>> getRsList(@RequestParam(required = false) Integer start, @RequestParam (required = false) Integer end) {
    if (start == null) {
      start = 1;
    }
    if (end == null) {
      end = rsService.size();
    }
    if (start < 1 || end > rsService.size() || start > end) {
      throw new RsEventNotValidException("invalid request param");
    }
    return ResponseEntity.ok(rsService.getSubList(start - 1, end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity<Void> addRsEvent(@RequestBody @Valid RsEvent rsEvent){
    int eventId = rsService.addRsEvent(rsEvent);
    return ResponseEntity.status(HttpStatus.CREATED).header("eventId", String.valueOf(eventId)).build();
  }

  @GetMapping("/rs/{eventId}")
  public ResponseEntity<RsEventReturn> getOneRsEvent(@PathVariable int eventId) {
    return ResponseEntity.ok().body(rsService.get(eventId));
  }

  @PatchMapping("/rs/{eventId}")
  public ResponseEntity<Void> updateRsEventOn(@RequestBody @Valid RsEventUpdate update, @PathVariable int eventId) {
    rsService.updateRsEventOn(update, eventId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/{eventId}")
  public ResponseEntity<Void> deleteRsEventOn(@PathVariable int eventId) {
    rsService.deleteRsEvent(eventId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/rs/vote/{eventId}")
  public ResponseEntity<Void> voteRsEventOn(@RequestBody @Valid Vote vote, @PathVariable int eventId) {
    rsService.voteTo(vote, eventId);
    return ResponseEntity.ok().build();
  }
}
