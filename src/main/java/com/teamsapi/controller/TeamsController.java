package com.teamsapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    @Autowired
    TeamsService teamService;
    @GetMapping("/messages")
    public List<Message> messagesInChannel(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String top) throws JsonProcessingException {
        return this.teamService.messagesInChannel(teamId, channelId, top);
    }


    @GetMapping("/replies")
    public List<Message> repliesOnMessage(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId, @RequestParam String top) throws JsonProcessingException {
        return this.teamService.repliesOnMessage(teamId, channelId, messageId, top);
    }

    @GetMapping("/channelNames")
    public List<Channel> channelNames(@RequestParam String teamId) throws JsonProcessingException {
        return this.teamService.channelNames(teamId);
    }

    @GetMapping("/message")
    public Message getMessageById(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) throws JsonProcessingException {
        return this.teamService.getMessageById(teamId, channelId, messageId);
    }

    @GetMapping("/summarizeReplies")
    public String summarizeReplies(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) throws JsonProcessingException {
        return this.teamService.summarizeReplies(teamId, channelId, messageId);
    }


}