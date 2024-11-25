package com.example.petpal.controller;

import com.example.petpal.business.IUserService;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.controller.converters.UserConverter;
import com.example.petpal.controller.dto.CreateEntityResponse;
import com.example.petpal.controller.dto.RegisterDTO;
import com.example.petpal.controller.dto.user.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(value = "userId") final long userId) {
        try {
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            UserDTO userDTO = UserConverter.convertFromUserToUserDTO(userOptional.get());
            return ResponseEntity.ok(userDTO);
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
    }

    @PostMapping
    public ResponseEntity<CreateEntityResponse> createUser(@RequestBody RegisterDTO userDTO) {
        User user = UserConverter.convertFromRegisterDTOToUser(userDTO);
        Long createdUserId = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(createdUserId).build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO) {
        try {
            User updatedUser = UserConverter.convertFromUserDTOToUser(userDTO);
            userService.updateUser(id, updatedUser);
            return ResponseEntity.noContent().build();
        } catch (InvalidUserException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
