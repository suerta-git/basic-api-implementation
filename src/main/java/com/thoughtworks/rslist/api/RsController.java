package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.service.RsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RsController {
  private final RsService rsService;

  public RsController(RsService rsService) {
    this.rsService = rsService;
  }

  @GetMapping("/list")
  public List<String> getList() {
    return rsService.getList();
  }
}
