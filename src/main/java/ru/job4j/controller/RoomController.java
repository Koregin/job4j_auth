package ru.job4j.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.repository.RoomRepository;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RestTemplate restTemplate;
    private final RoomRepository roomRepository;

    private static final String API = "http://localhost:8080/message";
    private static final String API_ID = "http://localhost:8080/message/{id}";

    public RoomController(RestTemplate restTemplate, RoomRepository rooms) {
        this.restTemplate = restTemplate;
        this.roomRepository = rooms;
    }

    @GetMapping("")
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @PostMapping("")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        if (room.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be 0", HttpStatus.NOT_ACCEPTABLE);
        }
        if (room.getName() == null || room.getName().trim().length() == 0) {
            return new ResponseEntity("missed param: name", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(roomRepository.save(room));
    }

    @PutMapping("")
    public ResponseEntity<Room> update(@RequestBody Room room) {
        if (room.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (room.getName() == null || room.getName().trim().length() == 0) {
            return new ResponseEntity("missed param: name", HttpStatus.NOT_ACCEPTABLE);
        }
        roomRepository.save(room);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int roomId) {
        /* Get all messages for room */
        List<Message> messages = restTemplate.exchange(
                API + "?roomId=" + roomId,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Message>>() {
                }).getBody();
        /* Delete all messages for room */
        if (messages != null) {
            for (Message message : messages) {
                restTemplate.delete(API_ID, message.getId());
            }
        }
        /* Delete room */
        try {
            roomRepository.deleteById(roomId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("roomId = " + roomId + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
