package com.x4mv.listit.repository;

import com.x4mv.listit.model.Event;
import com.x4mv.listit.model.User;
import com.x4mv.listit.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    
    List<Event> findByCreador(User creador);
    
    List<Event> findByCreadorId(Integer creadorId);
    
    List<Event> findByRoomId(Integer roomId);
    
    List<Event> findByRoom(Room room);
    
  
}