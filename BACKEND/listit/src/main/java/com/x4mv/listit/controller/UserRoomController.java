package com.x4mv.listit.controller;

import com.x4mv.listit.dto.UserRoomDTO;
import com.x4mv.listit.dto.UserRoomResponseDTO;
import com.x4mv.listit.model.UserRoom;
import com.x4mv.listit.service.UserRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-rooms")
@CrossOrigin(origins = "*")
public class UserRoomController {
    
    @Autowired
    private UserRoomService userRoomService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<UserRoomResponseDTO> createUserRoom(@RequestBody UserRoomDTO userRoomDTO) {
        try {
            UserRoomResponseDTO createdUserRoom  = userRoomService.createUserRoom(userRoomDTO);
            return new ResponseEntity<>(createdUserRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // READ
    @GetMapping
    public ResponseEntity<List<UserRoomResponseDTO>> getAllUserRooms() {
        try {
            List<UserRoomResponseDTO> userRooms = userRoomService.getAllUserRooms();
            if (userRooms.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<UserRoomResponseDTO> getUserRoomById(
            @PathVariable("userId") Integer userId, 
            @PathVariable("roomId") Integer roomId) {
        Optional<UserRoomResponseDTO> userRoom = userRoomService.getUserRoomById(userId, roomId);
        return userRoom.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoomResponseDTO>> getUserRoomsByUserId(@PathVariable("userId") Integer userId) {
        try {
            List<UserRoomResponseDTO> userRooms = userRoomService.getUserRoomsByUserId(userId);
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<UserRoomResponseDTO>> getUserRoomsByRoomId(@PathVariable("roomId") Integer roomId) {
        try {
            List<UserRoomResponseDTO> userRooms = userRoomService.getUserRoomsByRoomId(roomId);
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // DELETE
    @DeleteMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<HttpStatus> deleteUserRoom(
            @PathVariable("userId") Integer userId, 
            @PathVariable("roomId") Integer roomId) {
        try {
            boolean deleted = userRoomService.deleteUserRoom(userId, roomId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
