package com.teamsapi.controller;

import com.teamsapi.service.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatGptController {
    @Autowired
    ChatGptService chatGptService;

}
