package com.example.service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;
    public Message createMessage(Message message) {
        // Validate message length
        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            return null;
        }
        if (message.getMessageText().length() > 255) {
            return null;
        }
        // Ensure user exists
        if (!accountRepository.existsById(message.getPostedBy())) {
            return null;
        }
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
        if (messageOptional.isPresent() && newText != null && !newText.isBlank() && newText.length() <= 255) {
            Message message = messageOptional.get();
            message.setMessageText(newText);
            return Optional.of(messageRepository.save(message));
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
