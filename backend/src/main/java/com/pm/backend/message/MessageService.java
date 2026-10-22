package com.pm.backend.message;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final ApplicationEventPublisher eventPublisher;


    public MessageService(
            MessageRepository repository,
            ApplicationEventPublisher eventPublisher
    ){
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> findAll(){
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(MessageResponse::from)
                .toList();
    }
}
