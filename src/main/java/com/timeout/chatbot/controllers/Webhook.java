package com.timeout.chatbot.controllers;

import com.timeout.chatbot.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Webhook {
    private final ApplicationConfig applicationConfig;

    @Autowired
    public Webhook(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return applicationConfig.getMessenger().getApp().getPageAccessToken();
    }
}
