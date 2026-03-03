package com.audit.notification.controller;

import com.audit.notification.services.EmailLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email-logs")
public class EmailLogController {

    private final EmailLogService emailLogService;

    public EmailLogController(EmailLogService emailLogService) {
        this.emailLogService = emailLogService;
    }

}
