package com.example.service;
@Service
public class MessageService {
    private MessageRepository messageRepository;
    public void setMessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
}
