package com.example.chatapp.model;

public class Message {
    private String message;
    private String username;
    private long timestamp;

    // Пустой конструктор для Firebase или других баз данных
    public Message() {
    }

    public Message(String message, String username, long timestamp) {
        this.message = message;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
