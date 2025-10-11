package com.x4mv.listit.repository;

import com.x4mv.listit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByNombre(String nombre);
    
    Optional<User> findByCorreo(String correo);
    
    boolean existsByNombre(String nombre);
    
    boolean existsByCorreo(String correo);
    
    @Query("SELECT u FROM User u WHERE u.nombre LIKE %:nombre%")
    Iterable<User> findByNombreContaining(@Param("nombre") String nombre);
}