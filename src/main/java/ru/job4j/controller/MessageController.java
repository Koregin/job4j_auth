package ru.job4j.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.repository.MessageRepository;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messages) {
        this.messageRepository = messages;
    }

    /*
    Get all messages
    If you want messages only for one room,
    then request should be like this: http://localhost:8080/message?roomId=1
     */
    @GetMapping("")
    public List<Message> getAllEmployee(@RequestParam(defaultValue = "0") int roomId) {
        if (roomId == 0) {
            return messageRepository.findAll();
        }
        return messageRepository.findByRoomId(roomId);
    }

    /*
    Create message
     */
    @PostMapping("")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        if (message.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be 0", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getMessage() == null || message.getMessage().trim().length() == 0) {
            return new ResponseEntity("missed param: message", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getRoomId() == 0) {
            return new ResponseEntity("missed param: roomId", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getPersonId() == 0) {
            return new ResponseEntity("missed param: personId", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(messageRepository.save(message));
    }

    /*
    Update message
     */
    @PutMapping("")
    public ResponseEntity<Message> update(@RequestBody Message message) {
        if (message.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getMessage() == null || message.getMessage().trim().length() == 0) {
            return new ResponseEntity("missed param: message", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getRoomId() == 0) {
            return new ResponseEntity("missed param: roomId", HttpStatus.NOT_ACCEPTABLE);
        }
        if (message.getPersonId() == 0) {
            return new ResponseEntity("missed param: personId", HttpStatus.NOT_ACCEPTABLE);
        }
        messageRepository.save(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    Delete message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int messageId) {
        try {
            messageRepository.deleteById(messageId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("messageId = " + messageId + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
