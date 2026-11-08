package com.danicode.notification_system.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationEvent {

    private String id;
    private String source;
    private String message;
    private Priority priority;
    private LocalDateTime timestamp;
    private NotificationStatus status;
}
