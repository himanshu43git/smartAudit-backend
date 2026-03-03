package com.audit.notification.controller;

import com.audit.notification.services.PushLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/push-logs")
public class PushLogController {

    private final PushLogService pushLogService;

    private PushLogController(PushLogService pushLogService) {
        this.pushLogService = pushLogService;
    }
}
