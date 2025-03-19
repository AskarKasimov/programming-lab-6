package ru.askar.common;

import java.io.Serializable;

public class Message implements Serializable {
    private int priority;
    private String message;

    public Message(int priority, String message) {
        this.priority = priority;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" + "priority=" + priority + ", message='" + message + '\'' + '}';
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
