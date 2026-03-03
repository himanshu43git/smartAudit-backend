package com.audit.notification.controller;

import com.audit.notification.services.SmsLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms-logs")
public class SmsLogController {

    private final SmsLogService smsLogService;

    public SmsLogController(SmsLogService smsLogService) {
        this.smsLogService = smsLogService;
    }

}
