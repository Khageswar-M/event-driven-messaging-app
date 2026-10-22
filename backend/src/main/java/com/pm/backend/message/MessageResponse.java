package com.pm.backend.message;

import java.time.Instant;

public record MessageResponse(
        Long id,
        String content,
        Instant createdAt
) {

    public static MessageResponse from(Message message){
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
