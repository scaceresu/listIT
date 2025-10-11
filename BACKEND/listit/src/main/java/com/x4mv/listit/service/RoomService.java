package com.x4mv.listit.service;

import com.x4mv.listit.dto.RoomDTO;
import com.x4mv.listit.dto.RoomResponseDTO;
import com.x4mv.listit.model.Room;
import com.x4mv.listit.repository.RoomRepository;

import jakarta.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private ModelMapper modelMapper;

   

    @PostConstruct
    private void configureMapper(){
        // configurar de DTO a entity 
        modelMapper.emptyTypeMap(RoomDTO.class, Room.class)
            .addMappings(mapper -> {
                mapper.map(RoomDTO::getNombre, Room::setNombre);
                mapper.map(RoomDTO::getDescription, Room::setDescription);
                mapper.map(RoomDTO::getFechaCreacion, Room::setFechaCreacion);
                // Ignoramos el id si es una creación
                mapper.skip(Room::setId);
            });

        // configurar de Entity a responseDto
        modelMapper.emptyTypeMap(Room.class, RoomResponseDTO.class)
            .addMappings(mapper -> {
                mapper.map(Room::getId, RoomResponseDTO::setId);
                mapper.map(Room::getNombre, RoomResponseDTO::setNombre);
                mapper.map(Room::getDescription, RoomResponseDTO::setDescription);
                mapper.map(Room::getFechaCreacion, RoomResponseDTO::setFechaCreacion);
            });
    }
    
    // CREATE
    public RoomResponseDTO createRoom(RoomDTO roomDTO) {
        // Configurar fecha de creación si no está presente
        if (roomDTO.getFechaCreacion() == null) {
            roomDTO.setFechaCreacion(LocalDateTime.now());
        }
        
        // volvemos entidad el dto
        Room room = modelMapper.map(roomDTO, Room.class);
        Room savedRoom = roomRepository.save(room);

        // la entidad guardada la pasamos a response
        RoomResponseDTO dto = modelMapper.map(savedRoom, RoomResponseDTO.class);
    
        return dto;
    }
    
    // READ
    public List<RoomResponseDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> modelMapper.map(room, RoomResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    public Optional<RoomResponseDTO> getRoomById(Integer id) {
        return roomRepository.findById(id)
                .map(room -> modelMapper.map(room, RoomResponseDTO.class));
    }
    
    public Optional<RoomResponseDTO> getRoomByNombre(String nombre) {
        return roomRepository.findByNombre(nombre)
                .map(room -> modelMapper.map(room, RoomResponseDTO.class));
    }
  
    // UPDATE
    public Optional<RoomResponseDTO> updateRoom(Integer id, RoomDTO roomDTO) {
        return roomRepository.findById(id)
                .map(existingRoom -> {
                    existingRoom.setNombre(roomDTO.getNombre());
                    existingRoom.setDescription(roomDTO.getDescription());
                    // No actualizamos fechaCreacion ya que debe mantenerse original
                    Room updatedRoom = roomRepository.save(existingRoom);
                    return modelMapper.map(updatedRoom, RoomResponseDTO.class);
                });
    }
    
    // DELETE
    public boolean deleteRoom(Integer id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY METHODS
    public boolean existsByNombre(String nombre) {
        return roomRepository.existsByNombre(nombre);
    }
    
    public List<RoomResponseDTO> getRoomsByNombreContaining(String nombre) {
        return roomRepository.findByNombreContaining(nombre).stream()
                .map(room -> modelMapper.map(room, RoomResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    public List<RoomResponseDTO> getRoomsByUserId(Integer userId) {
        return roomRepository.findRoomsByUserId(userId).stream()
                .map(room -> modelMapper.map(room, RoomResponseDTO.class))
                .collect(Collectors.toList());
    }
    
 
}
