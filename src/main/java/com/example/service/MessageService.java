package com.example.service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Get a specific message by its ID
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    // Get all messages from a user
    public List<Message> getMessagesByUser(Integer postedBy) {
        return messageRepository.findByPostedBy(postedBy);
    }

    // Update an existing message
    public Optional<Message> updateMessage(Integer messageId, String newText) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setMessageText(newText);
            messageRepository.save(message);
            return Optional.of(message);
        }
        return Optional.empty(); // Return empty if the message doesn't exist
    }

    // Delete a message
    public boolean deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }
}
