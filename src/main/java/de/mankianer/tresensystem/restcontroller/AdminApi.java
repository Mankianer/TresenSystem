package de.mankianer.tresensystem.restcontroller;

import de.mankianer.tresensystem.exeptions.UserExistsException;
import de.mankianer.tresensystem.exeptions.UserMissingException;
import de.mankianer.tresensystem.exeptions.UserNotFoundException;
import de.mankianer.tresensystem.restcontroller.dto.CreateUserDTO;
import de.mankianer.tresensystem.restcontroller.dto.ResponseUserDTO;
import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.services.UserService;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminApi {

    private final UserService userService;


    public AdminApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/user/{username}")
    public ResponseUserDTO getUser(@PathVariable String username) throws UserNotFoundException {
        return convertUserToResponseUserDTO(userService.findUser(username));
    }

    @PostMapping("/admin/user/{username}")
    public ResponseUserDTO createUser(@PathVariable String username, @RequestBody CreateUserDTO userDTO, @RequestParam(defaultValue = "false") boolean isPasswordHashed) throws UserExistsException {
        User user = userService.newUser(username, userDTO.getPassword(), isPasswordHashed, userDTO.getAuthorities());
        return convertUserToResponseUserDTO(userService.createUser(user));
    }

    @PutMapping("/admin/user/{username}")
    public ResponseUserDTO updateUser(@PathVariable String username, @RequestBody CreateUserDTO userDTO, @RequestParam(defaultValue = "false") boolean isPasswordHashed) throws UserMissingException {
        User user = userService.newUser(username, userDTO.getPassword(), isPasswordHashed, userDTO.getAuthorities());
        return convertUserToResponseUserDTO(userService.updateUser(user));
    }

    @DeleteMapping("/admin/user/{username}")
    public ResponseUserDTO deleteUser(@PathVariable String username) throws UserNotFoundException {
        return convertUserToResponseUserDTO(userService.deactivateUser(username));
    }

    private ResponseUserDTO convertUserToResponseUserDTO(User user) {
        return new ResponseUserDTO(user.getUsername(), user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()), user.isEnabled());
    }
}
