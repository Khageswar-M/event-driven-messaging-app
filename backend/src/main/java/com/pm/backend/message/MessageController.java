package com.pm.backend.message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Create and retrieve messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping
    @Operation(summary = "Get all messages, newest first")
    public List<MessageResponse> findAll(){
        return messageService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a message and broadcast a live event")
    public MessageResponse create(@Valid @RequestBody CreateMessageRequest request){
        return messageService.create(request);
    }
}
