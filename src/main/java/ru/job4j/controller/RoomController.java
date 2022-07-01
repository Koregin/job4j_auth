package ru.job4j.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Room;
import ru.job4j.repository.RoomRepository;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository rooms) {
        this.roomRepository = rooms;
    }

    @GetMapping("/")
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        if (room.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be 0", HttpStatus.NOT_ACCEPTABLE);
        }
        if (room.getName() == null || room.getName().trim().length() == 0) {
            return new ResponseEntity("missed param: name", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(roomRepository.save(room));
    }

    @PutMapping("/")
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
        try {
            roomRepository.deleteById(roomId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("roomId = " + roomId + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
