package com.x4mv.listit.service;

import com.x4mv.listit.dto.LoginDTO;
import com.x4mv.listit.dto.LoginResponseDTO;
import com.x4mv.listit.dto.UserDTO;
import com.x4mv.listit.dto.UserResponseDTO;
import com.x4mv.listit.model.User;
import com.x4mv.listit.model.TypeRole;
import com.x4mv.listit.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import com.x4mv.listit.repository.TypeRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeRoleRepository typeRoleRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Inyectamos la instancia de de bcrypt para hashear la contrasena
    @Autowired 
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void configureMapping() {
        // Mapear de UserDTO a User (ignorando campos específicos)
        modelMapper.emptyTypeMap(UserDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.map(UserDTO::getNombre, User::setNombre);
                    mapper.map(UserDTO::getEdad, User::setEdad);
                    mapper.map(UserDTO::getCorreo, User::setCorreo);
                    // ignoramos la contrasena por que la debemos encriptar
                    mapper.skip(User::setContrasena);
                    // Ignoramos el mapeo directo de rolId, lo manejaremos manualmente
                    mapper.skip(User::setRol);
                    // Ignoramos el id si es una creación
                    mapper.skip(User::setId);
                });

        // Mapear de User a UserResponseDTO
        modelMapper.emptyTypeMap(User.class, UserResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.map(User::getId, UserResponseDTO::setId);
                    mapper.map(User::getNombre, UserResponseDTO::setNombre);
                    mapper.map(User::getEdad, UserResponseDTO::setEdad);
                    mapper.map(User::getCorreo, UserResponseDTO::setCorreo);
                    // Mapeo manual del rol
                    mapper.map(src -> src.getRol().getId(), UserResponseDTO::setRolId);
                    mapper.map(src -> src.getRol().getNombre(), UserResponseDTO::setRolName);
                });
    }

    // LOGIN USER
    public LoginResponseDTO loginUser(LoginDTO loginDTO){

        // verificamos que exista un usuario con ese correo y contrasena 
        User usuario = userRepository.findByCorreo(loginDTO.getCorreo())
            .orElseThrow(() ->{
                throw new RuntimeException("El email o el password son incorrectos");
        });

        // verificamos que los hashes sean iguales 
        Boolean validPassword = passwordEncoder.matches(loginDTO.getContrasena(), usuario.getContrasena());

        if (validPassword == false ){
            throw new RuntimeException("El email o el password son incorrectos");
        }

        // instanciamos el response para devolver al cliente 
        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setRolId(usuario.getRol().getId());
        loginResponse.setRolNombre(usuario.getRol().getNombre());

        return loginResponse;


        
    }

    // CREATE
    public UserResponseDTO createUser(UserDTO userDTO) {

        // verificamos si no existe un usuario con ese mismo correo
        Optional<User> usuarioExistente = userRepository.findByCorreo(userDTO.getCorreo());
        if (usuarioExistente.isPresent()) {

            throw new RuntimeException("El correo ya existe");
        }

        TypeRole rolExistente = typeRoleRepository.findById(userDTO.getRolId())
                .orElseThrow(() -> {
                    throw new RuntimeException("El rol que quieres asignar no existe");
                });

        // empezamos a mapear el dto al entity
        User nuevoUsuario = modelMapper.map(userDTO, User.class);
        nuevoUsuario.setRol(rolExistente);
        nuevoUsuario.setContrasena(passwordEncoder.encode(userDTO.getContrasena()));


        // guardamos en la bd
        User usuarioGuardado = userRepository.save(nuevoUsuario);

        // mapeamos de entity a response
        return modelMapper.map(usuarioGuardado, UserResponseDTO.class);
    }

    // READ
    public List<UserResponseDTO> getAllUsers() {

        // encontramos todos los usuarios
        List<User> usuarios = userRepository.findAll();

        // mapeamos a responseDTO
        List<UserResponseDTO> usuariosResponse = new ArrayList<>();

        // recorremos los usuarios para mapearlos de entity a response
        for (User user : usuarios) {
            UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
            usuariosResponse.add(dto);
        }

        return usuariosResponse;
    }

    public UserResponseDTO getUserById(Integer id) {

        // verificamos que exista el usuario
        User usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RuntimeException("El usuario no existe");
                });

        UserResponseDTO usuarioResponse = modelMapper.map(usuarioExistente, UserResponseDTO.class);

        return usuarioResponse;
    }

    // UPDATE
    public UserResponseDTO updateUser(Integer id, UserDTO userDTO) {

        // verificamos que exista el usuario
        User usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RuntimeException("El usuario no existe");
                });

        TypeRole rolExistente = typeRoleRepository.findById(userDTO.getRolId())
                .orElseThrow(() -> {
                    throw new RuntimeException("El rol que quieres asignar no existe");
                });

        // mapeamos el dto a entity
        User usuarioActualizado = modelMapper.map(userDTO, User.class);
        usuarioActualizado.setId(id); // Mantenemos el ID del usuario existente
        usuarioActualizado.setRol(rolExistente); // asignamos el rol existente

        // guardamos en la db
        User usuarioGuardado = userRepository.save(usuarioActualizado);

        // mapeamos el entity a response
        UserResponseDTO dto = modelMapper.map(usuarioGuardado, UserResponseDTO.class);

        return dto;
    }

    // DELETE
    public void deleteUser(Integer id) {

        // verificamos que exista el usuario

        User usuarioExistente = userRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("El usuario no existe");
        });

        userRepository.delete(usuarioExistente);

    }

}
