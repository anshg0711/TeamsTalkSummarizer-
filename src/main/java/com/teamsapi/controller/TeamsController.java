package com.teamsapi.controller;

import com.teamsapi.custom_annotation.annotation.MethodExecutionTime;
import com.teamsapi.entity.teamsapi.Channel;
import com.teamsapi.entity.teamsapi.Message;
import com.teamsapi.service.TeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")

public class TeamsController {
    private final TeamsService teamService;
    @Autowired
    public TeamsController(TeamsService teamService) {
        this.teamService = teamService;
    }


    @GetMapping("/messages")
    @MethodExecutionTime
    public List<Message> getMessagesInChannel(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String top) {
        return teamService.getMessagesInChannel(teamId, channelId, top);
    }

    @GetMapping("/replies")
    @MethodExecutionTime(threshold = 1)
    public List<Message> getRepliesOnMessage(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) {
        return teamService.getRepliesOnMessage(teamId, channelId, messageId);
    }

    @GetMapping("/channelNames")
    @MethodExecutionTime
    public List<Channel> getChannelNames(@RequestParam String teamId) {
        return teamService.getAllChannels(teamId);
    }

    @GetMapping("/message")
    @MethodExecutionTime
    public Message getMessageByIdentifier(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) {
        return teamService.getMessageByIdentifier(teamId, channelId, messageId);
    }

    @GetMapping("/summarizeReplies")
    @MethodExecutionTime
    public String summarizeRepliesForMessage(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) {
       return teamService.summarizeRepliesForMessage(teamId, channelId, messageId);
    }


}

