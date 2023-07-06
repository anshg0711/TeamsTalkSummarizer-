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
    @Autowired
    TeamsService teamService;
    @GetMapping("/messages")
    @MethodExecutionTime
    public List<Message> messagesInChannel(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String top)  {
        return this.teamService.messagesInChannel(teamId, channelId, top);
    }


    @GetMapping("/replies")
    @MethodExecutionTime(threshold = 1)
    public List<Message> repliesOnMessage(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId, @RequestParam String top) {
        return this.teamService.repliesOnMessage(teamId, channelId, messageId, top);
    }

    @GetMapping("/channelNames")
    @MethodExecutionTime
    public List<Channel> channelNames(@RequestParam String teamId){
        return this.teamService.channelNames(teamId);
    }
    @GetMapping("/message")
    @MethodExecutionTime
    public Message getMessageById(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId)  {
        return this.teamService.getMessageById(teamId, channelId, messageId);
    }

    @GetMapping("/summarizeReplies")
    @MethodExecutionTime
    public String summarizeReplies(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) {
        return this.teamService.summarizeReplies(teamId, channelId, messageId);
    }
    @GetMapping("/summarizeMessageLink")
    @MethodExecutionTime
    public String summarizeMessageLink(@RequestParam String messageLink){
        return this.teamService.summarizeByMessageLink(messageLink);
    }
}