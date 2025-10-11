package com.x4mv.listit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Integer id;
    private String nombre;
    private String description;
    private Integer precio;
    private Integer cantidad;
    private String comentario;
    private Boolean completada;
    private Integer encargadoId;
}