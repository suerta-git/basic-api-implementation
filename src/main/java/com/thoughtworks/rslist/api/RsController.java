package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.service.RsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  @Autowired
  RsService rsService;

  @GetMapping("/list")
  public List<String> getList() {
    return rsService.getList();
  }
}
