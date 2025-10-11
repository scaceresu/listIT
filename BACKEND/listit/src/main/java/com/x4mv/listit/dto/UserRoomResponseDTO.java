package com.x4mv.listit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoomResponseDTO {
    private Integer userId;
    private Integer roomId;
    private String userName;
    private String roomName;
    private Integer rolId;
    private String rolName;
}