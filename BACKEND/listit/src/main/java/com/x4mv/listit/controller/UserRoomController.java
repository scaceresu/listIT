package com.x4mv.listit.controller;

import com.x4mv.listit.dto.UserRoomDTO;
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
    public ResponseEntity<UserRoomDTO> createUserRoom(@RequestBody UserRoomDTO userRoomDTO) {
        try {
            UserRoomDTO createdUserRoom = userRoomService.createUserRoom(userRoomDTO);
            return new ResponseEntity<>(createdUserRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // READ
    @GetMapping
    public ResponseEntity<List<UserRoomDTO>> getAllUserRooms() {
        try {
            List<UserRoomDTO> userRooms = userRoomService.getAllUserRooms();
            if (userRooms.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<UserRoomDTO> getUserRoomById(
            @PathVariable("userId") Long userId, 
            @PathVariable("roomId") Long roomId) {
        Optional<UserRoomDTO> userRoom = userRoomService.getUserRoomById(userId, roomId);
        return userRoom.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoomDTO>> getUserRoomsByUserId(@PathVariable("userId") Long userId) {
        try {
            List<UserRoomDTO> userRooms = userRoomService.getUserRoomsByUserId(userId);
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<UserRoomDTO>> getUserRoomsByRoomId(@PathVariable("roomId") Long roomId) {
        try {
            List<UserRoomDTO> userRooms = userRoomService.getUserRoomsByRoomId(roomId);
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<UserRoomDTO>> getActiveUserRoomsByUserId(@PathVariable("userId") Long userId) {
        try {
            List<UserRoomDTO> userRooms = userRoomService.getActiveUserRoomsByUserId(userId);
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/room/{roomId}/active")
    public ResponseEntity<List<UserRoomDTO>> getActiveUserRoomsByRoomId(@PathVariable("roomId") Long roomId) {
        try {
            List<UserRoomDTO> userRooms = userRoomService.getActiveUserRoomsByRoomId(roomId);
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserRoomDTO>> getUserRoomsByRole(@PathVariable("role") UserRoom.UserRole role) {
        try {
            List<UserRoomDTO> userRooms = userRoomService.getUserRoomsByRole(role);
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/{userId}/role/{role}")
    public ResponseEntity<List<UserRoomDTO>> getUserRoomsByUserIdAndRole(
            @PathVariable("userId") Long userId, 
            @PathVariable("role") UserRoom.UserRole role) {
        try {
            List<UserRoomDTO> userRooms = userRoomService.getUserRoomsByUserIdAndRole(userId, role);
            return new ResponseEntity<>(userRooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/room/{roomId}/count")
    public ResponseEntity<Long> countActiveUsersByRoomId(@PathVariable("roomId") Long roomId) {
        try {
            Long count = userRoomService.countActiveUsersByRoomId(roomId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // UPDATE
    @PutMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<UserRoomDTO> updateUserRoom(
            @PathVariable("userId") Long userId, 
            @PathVariable("roomId") Long roomId, 
            @RequestBody UserRoomDTO userRoomDTO) {
        Optional<UserRoomDTO> updatedUserRoom = userRoomService.updateUserRoom(userId, roomId, userRoomDTO);
        return updatedUserRoom.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // DELETE
    @DeleteMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<HttpStatus> deleteUserRoom(
            @PathVariable("userId") Long userId, 
            @PathVariable("roomId") Long roomId) {
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