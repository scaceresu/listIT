package com.x4mv.listit.controller;

import com.x4mv.listit.dto.RoomDTO;
import com.x4mv.listit.dto.RoomResponseDTO;
import com.x4mv.listit.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomDTO roomDTO) {
        try {
            RoomResponseDTO createdRoom = roomService.createRoom(roomDTO);
            return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // READ
    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        try {
            List<RoomResponseDTO> rooms = roomService.getAllRooms();
            if (rooms.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable("id") Integer id) {
        Optional<RoomResponseDTO> room = roomService.getRoomById(id);
        return room.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/name/{nombre}")
    public ResponseEntity<RoomResponseDTO> getRoomByNombre(@PathVariable("nombre") String nombre) {
        Optional<RoomResponseDTO> room = roomService.getRoomByNombre(nombre);
        return room.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByNombreContaining(@RequestParam("nombre") String nombre) {
        try {
            List<RoomResponseDTO> rooms = roomService.getRoomsByNombreContaining(nombre);
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByUserId(@PathVariable("userId") Integer userId) {
        try {
            List<RoomResponseDTO> rooms = roomService.getRoomsByUserId(userId);
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable("id") Integer id, @RequestBody RoomDTO roomDTO) {
        Optional<RoomResponseDTO> updatedRoom = roomService.updateRoom(id, roomDTO);
        return updatedRoom.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Integer id) {
        try {
            boolean deleted = roomService.deleteRoom(id);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.OK).body("La sala ha sido eliminado con exito");
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // UTILITY ENDPOINTS
    @GetMapping("/exists/nombre/{nombre}")
    public ResponseEntity<Boolean> checkRoomNombreExists(@PathVariable("nombre") String nombre) {
        boolean exists = roomService.existsByNombre(nombre);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
