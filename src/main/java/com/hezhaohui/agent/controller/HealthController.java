package com.hezhaohui.agent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public Result health() {
        return new Result(Code.SUCCESS, "OK", "Healthy");
    }
}
