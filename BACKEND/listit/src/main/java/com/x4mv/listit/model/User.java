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
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String nombre;
    private Integer edad;
    @Column(unique = true, nullable = false)
    private String correo;
    @Column(nullable = false)
    private String contrasena;
    @ManyToOne
    @JoinColumn(name = "rol")
    private TypeRole rol;
   
}
