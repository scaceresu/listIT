package com.x4mv.listit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;

@Entity
@Table(name = "user_room")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoom {
    
    @EmbeddedId
    private UserRoomId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "usuario_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "sala_id")
    private Room room;
    
    @ManyToOne
    @JoinColumn(name = "rol")
    private TypeRole rol;
 
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRoomId implements Serializable {
        
        private Integer userId; 
        private Integer roomId;
    }
}
