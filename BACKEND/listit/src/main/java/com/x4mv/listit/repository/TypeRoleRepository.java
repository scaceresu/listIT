package com.x4mv.listit.repository;

import com.x4mv.listit.model.TypeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRoleRepository extends JpaRepository<TypeRole, Integer> {
    
    Optional<TypeRole> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}