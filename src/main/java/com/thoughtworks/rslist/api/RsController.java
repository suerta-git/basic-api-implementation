package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private final List<RsEvent> rsList = new ArrayList<>(Arrays.asList(
          new RsEvent("第一条事件", "无标签"),
          new RsEvent("第二条事件", "无标签"),
          new RsEvent("第三条事件", "无标签")));

  @GetMapping("/rs/list")
  public List<RsEvent> getRsList(@RequestParam(required = false) Integer start, @RequestParam (required = false) Integer end) {
    if (start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start - 1, end);
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
  }

  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index) {
    return rsList.get(index - 1);
  }
}
