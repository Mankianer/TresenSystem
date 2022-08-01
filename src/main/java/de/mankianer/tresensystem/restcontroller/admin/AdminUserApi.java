package de.mankianer.tresensystem.restcontroller.admin;

import de.mankianer.tresensystem.exeptions.UserExistsException;
import de.mankianer.tresensystem.exeptions.UserMissingException;
import de.mankianer.tresensystem.exeptions.UserNotFoundException;
import de.mankianer.tresensystem.restcontroller.dto.CreateUserDTO;
import de.mankianer.tresensystem.restcontroller.dto.ResponseUserDTO;
import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/user/")
public class AdminUserApi {

    private final UserService userService;


    public AdminUserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<ResponseUserDTO> getUser() {
        return userService.findAll().stream().map(user -> userService.convertUserToResponseUserDTO(user)).collect(Collectors.toUnmodifiableList());
    }

    @GetMapping("{username}")
    public ResponseUserDTO getUser(@PathVariable String username) throws UserNotFoundException {
        return userService.convertUserToResponseUserDTO(userService.findUser(username));
    }

    @PostMapping("{username}")
    public ResponseUserDTO createUser(@PathVariable String username, @RequestBody CreateUserDTO userDTO, @RequestParam(defaultValue = "false") boolean isPasswordHashed) throws UserExistsException {
        User user = userService.newUserObject(username, userDTO.getPassword(), isPasswordHashed, userDTO.getAuthorities());
        return userService.convertUserToResponseUserDTO(userService.createUser(user));
    }

    @PutMapping("{username}")
    public ResponseUserDTO updateUser(@PathVariable String username, @RequestBody CreateUserDTO userDTO, @RequestParam(defaultValue = "false") boolean isPasswordHashed) throws UserMissingException {
        User user = userService.newUserObject(username, userDTO.getPassword(), isPasswordHashed, userDTO.getAuthorities());
        return userService.convertUserToResponseUserDTO(userService.updateUser(user));
    }

    @DeleteMapping("{username}")
    public ResponseUserDTO deleteUser(@PathVariable String username) throws UserNotFoundException {
        return userService.convertUserToResponseUserDTO(userService.deactivateUser(username));
    }

}
