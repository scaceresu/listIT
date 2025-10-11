package com.x4mv.listit.service;

import com.x4mv.listit.dto.TypeRoleDTO;
import com.x4mv.listit.dto.TypeRoleResponseDTO;
import com.x4mv.listit.model.TypeRole;
import com.x4mv.listit.repository.TypeRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TypeRoleService {
    
    @Autowired
    private TypeRoleRepository typeRoleRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // CREATE
    public TypeRoleResponseDTO createTypeRole(TypeRoleDTO typeRoleDTO) {
        TypeRole typeRole = modelMapper.map(typeRoleDTO, TypeRole.class);
        TypeRole savedTypeRole = typeRoleRepository.save(typeRole);
        return modelMapper.map(savedTypeRole, TypeRoleResponseDTO.class);
    }
    
    // READ
    public List<TypeRoleResponseDTO> getAllTypeRoles() {
        return typeRoleRepository.findAll().stream()
                .map(typeRole -> modelMapper.map(typeRole, TypeRoleResponseDTO.class))
                .collect(Collectors.toList());
    }
    
    public Optional<TypeRoleResponseDTO> getTypeRoleById(Integer id) {
        return typeRoleRepository.findById(id)
                .map(typeRole -> modelMapper.map(typeRole, TypeRoleResponseDTO.class));
    }
    
    public Optional<TypeRoleResponseDTO> getTypeRoleByNombre(String nombre) {
        return typeRoleRepository.findByNombre(nombre)
                .map(typeRole -> modelMapper.map(typeRole, TypeRoleResponseDTO.class));
    }
    
    // UPDATE
    public Optional<TypeRoleResponseDTO> updateTypeRole(Integer id, TypeRoleDTO typeRoleDTO) {
        return typeRoleRepository.findById(id)
                .map(existingTypeRole -> {
                    existingTypeRole.setNombre(typeRoleDTO.getNombre());
                    TypeRole updatedTypeRole = typeRoleRepository.save(existingTypeRole);
                    return modelMapper.map(updatedTypeRole, TypeRoleResponseDTO.class);
                });
    }
    
    // DELETE
    public boolean deleteTypeRole(Integer id) {
        if (typeRoleRepository.existsById(id)) {
            typeRoleRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY METHODS
    public boolean existsByNombre(String nombre) {
        return typeRoleRepository.existsByNombre(nombre);
    }
}