package com.x4mv.listit.repository;

import com.x4mv.listit.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    
    Optional<Room> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    @Query("SELECT r FROM Room r WHERE r.nombre LIKE %:nombre%")
    List<Room> findByNombreContaining(@Param("nombre") String nombre);
    
    @Query("SELECT r FROM Room r JOIN UserRoom ur ON r.id = ur.room.id WHERE ur.user.id = :userId")
    List<Room> findRoomsByUserId(@Param("userId") Integer userId);
}