package com.example.myfirstrestapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {

        // generate secret
        user.setSecret(UUID.randomUUID().toString());
        var savedUser = userRepository.save(user);
        return new ResponseEntity<User>(savedUser,HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam(value = "id")int id) {
        var user = userRepository.findById(id);
        if(user.isPresent()) {
            return new ResponseEntity<User>(user.get(),HttpStatus.OK);
        }
        return new ResponseEntity("No User found with id " + id, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/validate")
    private ResponseEntity<String> validate(@RequestParam(value = "email")String email,
                                         @RequestParam(value = "password")String password) {
        var validUser = userRepository.findByEmailAndPassword(email,password);
        if(validUser.isPresent()) {
            return new ResponseEntity<String>("API Secret: " + validUser.get().getSecret(),HttpStatus.OK);
        }
        return new ResponseEntity<String>("Wrong credentials/No Account found", HttpStatus.NOT_FOUND);
    }

}
