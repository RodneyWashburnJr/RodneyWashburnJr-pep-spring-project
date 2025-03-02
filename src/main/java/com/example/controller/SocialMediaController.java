package com.example.controller;
import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.service.MessageService;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
@RequestMapping()
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;
    

    // User Registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            Account registeredAccount = accountService.register(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(registeredAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Optional<Account> user = accountService.login(account.getUsername(), account.getPassword());
    
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());  
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Create New Message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        
        Message createdMessage = messageService.createMessage(message);
        
       
        if (createdMessage == null) {
            return ResponseEntity.badRequest().build();
        }
        
        
        return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
    }

    // Get All Messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // Get One Message by ID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        return message.isPresent() ? ResponseEntity.ok(message.get()) : ResponseEntity.status(200).body("");
    }

    // Delete Message by ID
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        int rowsDeleted = messageService.deleteMessage(messageId);
        if (rowsDeleted > 0) {
            return ResponseEntity.ok(rowsDeleted);  
        } else {
            return ResponseEntity.status(200).body("");
        }
    }

    /** Update Message by ID
     @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody String newText) {
        if (newText == null || newText.trim().isBlank()) {
            return ResponseEntity.status(400).body("");
        }
        int rowsUpdated = messageService.updateMessage(messageId, newText);
        if (rowsUpdated > 0) {
            return ResponseEntity.ok(rowsUpdated);  // 200 OK with user object
        } else {
            return ResponseEntity.status(400).body("");
        }
    }
        */

    // Get All Messages from User by Account ID
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesFromUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);
        return ResponseEntity.ok(messages);
    }

@PatchMapping("/messages/{messageId}")
public ResponseEntity<?> updateOrCreateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
    // Validate input
    if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
        return ResponseEntity.status(400).body("Message text cannot be empty");
    }
    if (message.getMessageText().length() > 255) {
        return ResponseEntity.status(400).body("Message text is too long");
    }

    // Check if the message exists
    Optional<Message> existingMessage = messageService.getMessageById(messageId);
    
    if (existingMessage.isPresent()) {
        int rowsUpdated = messageService.updateMessage(messageId, message.getMessageText());
        return ResponseEntity.ok(rowsUpdated); 
    } else {
        return ResponseEntity.status(400).body("Message not found"); 
    }
    
}
}

