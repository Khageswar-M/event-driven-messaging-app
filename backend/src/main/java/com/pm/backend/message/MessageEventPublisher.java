package com.pm.backend.message;

import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MessageEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageEventPublisher(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessageCreated(MessageCreateEvent event){
        messagingTemplate.convertAndSend("/topic/messages", event.message());
    }


}
