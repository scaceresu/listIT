package com.x4mv.listit.service;

import com.x4mv.listit.dto.UserResponseDTO;
import com.x4mv.listit.dto.UserRoomDTO;
import com.x4mv.listit.dto.UserRoomResponseDTO;
import com.x4mv.listit.model.UserRoom;
import com.x4mv.listit.model.User;
import com.x4mv.listit.model.Room;
import com.x4mv.listit.repository.UserRoomRepository;
import com.x4mv.listit.repository.UserRepository;
import com.x4mv.listit.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoomService {
    
    @Autowired
    private UserRoomRepository userRoomRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private ModelMapper modelMapper;


    // CREATE
    public UserRoomResponseDTO createUserRoom(UserRoomDTO userRoomDTO) {
        // Verify user and room exist
        Optional<User> user = userRepository.findById(userRoomDTO.getUserId());
        Optional<Room> room = roomRepository.findById(userRoomDTO.getRoomId());
        
        if (user.isPresent() && room.isPresent()) {
            // creamos el entity
            UserRoom userRoom = new UserRoom();

            // seteamos el id
            UserRoom.UserRoomId id = new UserRoom.UserRoomId(userRoomDTO.getUserId(), userRoomDTO.getRoomId());
            userRoom.setId(id);
            
            // seteamos los campos del entity
            userRoom.setUser(user.get());
            userRoom.setRoom(room.get());
            
            UserRoom savedUserRoom = userRoomRepository.save(userRoom);
            return convertToResponse(savedUserRoom);
        }
        
        throw new RuntimeException("User or Room not found");
    }
    
    // READ
    public List<UserRoomResponseDTO> getAllUserRooms() {
        return userRoomRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public Optional<UserRoomResponseDTO> getUserRoomById(Integer userId, Integer roomId) {
        UserRoom.UserRoomId id = new UserRoom.UserRoomId(userId, roomId);
        return userRoomRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    public List<UserRoomResponseDTO> getUserRoomsByUserId(Integer userId) {
        return userRoomRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<UserRoomResponseDTO> getUserRoomsByRoomId(Integer roomId) {
        return userRoomRepository.findByRoomId(roomId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public Optional<UserRoomResponseDTO> getUserRoomByUserIdAndRoomId(Integer userId, Integer roomId) {
        return userRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .map(this::convertToResponse);
    }
          
    // UPDATE
    public Optional<UserRoomResponseDTO> updateUserRoom(Integer userId, Integer roomId, UserRoomDTO userRoomDTO) {
        UserRoom.UserRoomId id = new UserRoom.UserRoomId(userId, roomId);
        return userRoomRepository.findById(id)
                .map(existingUserRoom -> {
                    UserRoom updatedUserRoom = userRoomRepository.save(existingUserRoom);
                    return convertToResponse(updatedUserRoom);
                });
    }
    
    // DELETE
    public boolean deleteUserRoom(Integer userId, Integer roomId) {
        UserRoom.UserRoomId id = new UserRoom.UserRoomId(userId, roomId);
        if (userRoomRepository.existsById(id)) {
            userRoomRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Convertir de entity a UserRoomResponseDTO 
    private UserRoomResponseDTO convertToResponse(UserRoom userRoom) {
        UserRoomResponseDTO dto = new UserRoomResponseDTO();
        dto.setUserName(userRoom.getUser().getNombre());
        dto.setUserId(userRoom.getId().getUserId());
        dto.setRoomId(userRoom.getId().getRoomId());
        dto.setRolId(userRoom.getRol().getId());;
        dto.setRolName(userRoom.getRol().getNombre());
        dto.setRoomName(userRoom.getRoom().getNombre());
       

        return dto;
    }

}
