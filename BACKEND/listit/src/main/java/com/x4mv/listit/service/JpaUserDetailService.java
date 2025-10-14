package com.x4mv.listit.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x4mv.listit.model.User;
import com.x4mv.listit.repository.TypeRoleRepository;
import com.x4mv.listit.repository.UserRepository;

@Service
public class JpaUserDetailService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeRoleRepository typeRoleRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException{

        Optional<User> usuario = userRepository.findByCorreo(correo);

        if (!usuario.isPresent()){
            throw new RuntimeException("El usuario no existe");
        }

        List<GrantedAuthority> authorities = typeRoleRepository.findAll().stream()
        .map(role -> new SimpleGrantedAuthority(role.getNombre()))
        .collect(Collectors.toList());


        return new org.springframework.security.core.userdetails.User(
            usuario.get().getCorreo(),
            usuario.get().getContrasena(),
            true,
            true,
            true,
            true,
            authorities 
        );
        

    }




}
