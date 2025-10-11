package com.x4mv.listit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer id;
    private String titulo;
    private String description;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer roomId;
    private Integer creadorId;
}