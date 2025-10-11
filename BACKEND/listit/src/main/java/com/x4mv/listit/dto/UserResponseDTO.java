package com.x4mv.listit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Integer id;
    private String nombre;
    private Integer edad;
    private String correo;
    private Integer rolId;
    private String rolName;
}