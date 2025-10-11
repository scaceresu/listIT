package com.x4mv.listit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoomDTO {
    private Integer userId;
    private Integer roomId;
    private Integer rolId;
}