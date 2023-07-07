package com.teamsapi.controller;

import com.teamsapi.custom_annotation.annotation.MethodExecutionTime;
import com.teamsapi.entity.teamsapi.Channel;
import com.teamsapi.entity.teamsapi.Message;
import com.teamsapi.service.TeamsService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("/teams")

public class TeamsController {
    private final TeamsService teamService;
    private final ExecutorService executorService;

    @Autowired
    public TeamsController(TeamsService teamService, ExecutorService executorService) {
        this.teamService = teamService;
        this.executorService = executorService;
    }

    @GetMapping("/messages")
    @MethodExecutionTime
    public List<Message> getMessagesInChannel(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String top) {
        Future<List<Message>> future = executorService.submit(() -> teamService.getMessagesInChannel(teamId, channelId, top));
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/replies")
    @MethodExecutionTime(threshold = 1)
    public List<Message> getRepliesOnMessage(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) {
        Future<List<Message>> future = executorService.submit(() -> teamService.getRepliesOnMessage(teamId, channelId, messageId));
        try {
            return future.get(); // Wait for the result of the asynchronous operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/channelNames")
    @MethodExecutionTime
    public List<Channel> getChannelNames(@RequestParam String teamId) {
        Future<List<Channel>> future = executorService.submit(() -> teamService.getAllChannels(teamId));
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/message")
    @MethodExecutionTime
    public Message getMessageByIdentifier(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) {
        Future<Message> future = executorService.submit(() -> teamService.getMessageByIdentifier(teamId, channelId, messageId));
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/summarizeReplies")
    @MethodExecutionTime
    public String summarizeRepliesForMessage(@RequestParam String teamId, @RequestParam String channelId, @RequestParam String messageId) {
        Future<String> future = executorService.submit(() -> teamService.summarizeRepliesForMessage(teamId, channelId, messageId));
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }


    @PreDestroy
    public void cleanUp() {
        executorService.shutdown();
    }
}

