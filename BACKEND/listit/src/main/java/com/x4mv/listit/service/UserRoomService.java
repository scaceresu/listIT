package com.x4mv.listit.service;

import com.x4mv.listit.dto.UserRoomDTO;
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
    public UserRoomDTO createUserRoom(UserRoomDTO userRoomDTO) {
        // Verify user and room exist
        Optional<User> user = userRepository.findById(userRoomDTO.getUserId());
        Optional<Room> room = roomRepository.findById(userRoomDTO.getRoomId());
        
        if (user.isPresent() && room.isPresent()) {
            UserRoom userRoom = new UserRoom();
            UserRoom.UserRoomId id = new UserRoom.UserRoomId(userRoomDTO.getUserId(), userRoomDTO.getRoomId());
            userRoom.setId(id);
            userRoom.setUser(user.get());
            userRoom.setRoom(room.get());
            userRoom.setRole(userRoomDTO.getRole());
            userRoom.setIsActive(userRoomDTO.getIsActive());
            
            UserRoom savedUserRoom = userRoomRepository.save(userRoom);
            return convertToDTO(savedUserRoom);
        }
        
        throw new RuntimeException("User or Room not found");
    }
    
    // READ
    public List<UserRoomDTO> getAllUserRooms() {
        return userRoomRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UserRoomDTO> getUserRoomById(Long userId, Long roomId) {
        UserRoom.UserRoomId id = new UserRoom.UserRoomId(userId, roomId);
        return userRoomRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<UserRoomDTO> getUserRoomsByUserId(Long userId) {
        return userRoomRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserRoomDTO> getUserRoomsByRoomId(Long roomId) {
        return userRoomRepository.findByRoomId(roomId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UserRoomDTO> getUserRoomByUserIdAndRoomId(Long userId, Long roomId) {
        return userRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .map(this::convertToDTO);
    }
    
    public List<UserRoomDTO> getActiveUserRoomsByUserId(Long userId) {
        return userRoomRepository.findByUserIdAndIsActive(userId, true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserRoomDTO> getActiveUserRoomsByRoomId(Long roomId) {
        return userRoomRepository.findByRoomIdAndIsActive(roomId, true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserRoomDTO> getUserRoomsByRole(UserRoom.UserRole role) {
        return userRoomRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserRoomDTO> getUserRoomsByUserIdAndRole(Long userId, UserRoom.UserRole role) {
        return userRoomRepository.findByUserIdAndRole(userId, role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Long countActiveUsersByRoomId(Long roomId) {
        return userRoomRepository.countActiveUsersByRoomId(roomId);
    }
    
    // UPDATE
    public Optional<UserRoomDTO> updateUserRoom(Long userId, Long roomId, UserRoomDTO userRoomDTO) {
        UserRoom.UserRoomId id = new UserRoom.UserRoomId(userId, roomId);
        return userRoomRepository.findById(id)
                .map(existingUserRoom -> {
                    existingUserRoom.setRole(userRoomDTO.getRole());
                    existingUserRoom.setIsActive(userRoomDTO.getIsActive());
                    UserRoom updatedUserRoom = userRoomRepository.save(existingUserRoom);
                    return convertToDTO(updatedUserRoom);
                });
    }
    
    // DELETE
    public boolean deleteUserRoom(Long userId, Long roomId) {
        UserRoom.UserRoomId id = new UserRoom.UserRoomId(userId, roomId);
        if (userRoomRepository.existsById(id)) {
            userRoomRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY METHODS
    private UserRoomDTO convertToDTO(UserRoom userRoom) {
        UserRoomDTO dto = new UserRoomDTO();
        dto.setUserId(userRoom.getId().getUserId());
        dto.setRoomId(userRoom.getId().getRoomId());
        dto.setRole(userRoom.getRole());
        dto.setJoinedAt(userRoom.getJoinedAt());
        dto.setIsActive(userRoom.getIsActive());
        
        if (userRoom.getUser() != null) {
            dto.setUsername(userRoom.getUser().getUsername());
        }
        
        if (userRoom.getRoom() != null) {
            dto.setRoomName(userRoom.getRoom().getName());
        }
        
        return dto;
    }
}