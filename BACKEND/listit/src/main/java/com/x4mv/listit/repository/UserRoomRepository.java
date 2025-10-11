package com.x4mv.listit.repository;

import com.x4mv.listit.model.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, UserRoom.UserRoomId> {
    
    @Query("SELECT ur FROM UserRoom ur WHERE ur.id.userId = :userId")
    List<UserRoom> findByUserId(@Param("userId") Integer userId);
    
    @Query("SELECT ur FROM UserRoom ur WHERE ur.id.roomId = :roomId")
    List<UserRoom> findByRoomId(@Param("roomId") Integer roomId);
    
    @Query("SELECT ur FROM UserRoom ur WHERE ur.id.userId = :userId AND ur.id.roomId = :roomId")
    Optional<UserRoom> findByUserIdAndRoomId(@Param("userId") Integer userId, @Param("roomId") Integer roomId);
    
   @Query("SELECT COUNT(ur) FROM UserRoom ur WHERE ur.id.roomId = :roomId")
    Long countUsersByRoomId(@Param("roomId") Integer roomId);
}
