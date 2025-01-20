package com.example.petpal.controller;

import com.example.petpal.business.IUserService;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidCredentialsException;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.controller.converters.UserConverter;
import com.example.petpal.controller.dto.CreateEntityResponse;
import com.example.petpal.controller.dto.RegisterDTO;
import com.example.petpal.controller.dto.user.UpdateUserDTOWithPassword;
import com.example.petpal.controller.dto.user.UpdateUserDTOWithoutPassword;
import com.example.petpal.controller.dto.user.UserDTO;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @RolesAllowed("Admin")
    public ResponseEntity<CreateEntityResponse> createUser(@RequestBody RegisterDTO userDTO) {
        User user = UserConverter.convertFromRegisterDTOToUser(userDTO);
        Long createdUserId = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(createdUserId).build());
    }

    @PutMapping("{id}/basic")
    @RolesAllowed({"Owner", "Veterinarian", "Admin"})
    public ResponseEntity<Void> updateUserWithoutPassword(@PathVariable long id, @RequestBody UpdateUserDTOWithoutPassword userDTO) {
        try {
            User updatedUser = UserConverter.convertFromUpdateUserDTOWithoutPasswordToUser(userDTO);
            userService.updateUserWithoutPassword(id, updatedUser);
            return ResponseEntity.noContent().build();
        } catch (InvalidUserException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("{id}/secure")
    @RolesAllowed({"Owner", "Veterinarian", "Admin"})
    public ResponseEntity<Void> updateUserWithPassword(@PathVariable long id, @RequestBody UpdateUserDTOWithPassword userDTO) {
        try {
            User updatedUser = UserConverter.convertFromUpdateUserDTOWithPasswordToUser(userDTO);
            userService.updateUser(id, userDTO.getOldPassword(), updatedUser);
            return ResponseEntity.noContent().build();
        } catch (InvalidUserException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("{id}")
    @RolesAllowed("Admin")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UnauthorizedDataAccessException e) {
            // Return 403 Forbidden if access is unauthorized
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
