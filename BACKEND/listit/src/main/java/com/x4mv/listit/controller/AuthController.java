package com.x4mv.listit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.x4mv.listit.service.UserService;
import com.x4mv.listit.dto.UserDTO;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController{


    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO){
        
        try {
            userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Usuario registrado con exito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+ e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
        }
    }

}
