package com.x4mv.listit.controller;

import com.x4mv.listit.dto.TypeRoleDTO;
import com.x4mv.listit.dto.TypeRoleResponseDTO;
import com.x4mv.listit.service.TypeRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-roles")
@CrossOrigin(origins = "*")
public class TypeRoleController {
    
    @Autowired
    private TypeRoleService typeRoleService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<TypeRoleResponseDTO> createTypeRole(@RequestBody TypeRoleDTO typeRoleDTO) {
        try {
            TypeRoleResponseDTO createdTypeRole = typeRoleService.createTypeRole(typeRoleDTO);
            return new ResponseEntity<>(createdTypeRole, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // READ
    @GetMapping
    public ResponseEntity<List<TypeRoleResponseDTO>> getAllTypeRoles() {
        try {
            List<TypeRoleResponseDTO> typeRoles = typeRoleService.getAllTypeRoles();
            if (typeRoles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(typeRoles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TypeRoleResponseDTO> getTypeRoleById(@PathVariable("id") Integer id) {
        Optional<TypeRoleResponseDTO> typeRole = typeRoleService.getTypeRoleById(id);
        return typeRole.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TypeRoleResponseDTO> getTypeRoleByNombre(@PathVariable("nombre") String nombre) {
        Optional<TypeRoleResponseDTO> typeRole = typeRoleService.getTypeRoleByNombre(nombre);
        return typeRole.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TypeRoleResponseDTO> updateTypeRole(@PathVariable("id") Integer id, @RequestBody TypeRoleDTO typeRoleDTO) {
        Optional<TypeRoleResponseDTO> updatedTypeRole = typeRoleService.updateTypeRole(id, typeRoleDTO);
        return updatedTypeRole.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTypeRole(@PathVariable("id") Integer id) {
        try {
            boolean deleted = typeRoleService.deleteTypeRole(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // UTILITY ENDPOINTS
    @GetMapping("/exists/nombre/{nombre}")
    public ResponseEntity<Boolean> checkNombreExists(@PathVariable("nombre") String nombre) {
        boolean exists = typeRoleService.existsByNombre(nombre);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}