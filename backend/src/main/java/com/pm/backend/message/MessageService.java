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

    @Transactional
    public MessageResponse create(CreateMessageRequest request){
        Message message = new Message();
        message.setContent(request.content().trim());

        Message saved = repository.saveAndFlush(message);
        MessageResponse response = MessageResponse.from(saved);

        // The event is delivered to the listener only after this transaction commits.
        eventPublisher.publishEvent(new MessageCreateEvent(response));
        return response;
    }
}
