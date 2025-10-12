package com.x4mv.listit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@Builder
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String description;
    private Integer precio;
    private Integer cantidad;
    private String comentario;
    private Boolean completada;

    @ManyToOne
    @JoinColumn(name = "encargado", nullable = false)
    private User encargado;
   
}
