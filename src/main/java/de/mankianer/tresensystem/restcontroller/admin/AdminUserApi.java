package de.mankianer.tresensystem.restcontroller.admin;

import de.mankianer.tresensystem.exeptions.UserExistsException;
import de.mankianer.tresensystem.exeptions.UserMissingException;
import de.mankianer.tresensystem.exeptions.UserNotFoundException;
import de.mankianer.tresensystem.restcontroller.dto.CreateUserDTO;
import de.mankianer.tresensystem.restcontroller.dto.ResponseUserDTO;
import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.security.entities.User;
import de.mankianer.tresensystem.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user/")
public class AdminUserApi {

    private final UserService userService;


    public AdminUserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{username}")
    public ResponseUserDTO getUser(@PathVariable String username) throws UserNotFoundException {
        return convertUserToResponseUserDTO(userService.findUser(username));
    }

    @PostMapping("{username}")
    public ResponseUserDTO createUser(@PathVariable String username, @RequestBody CreateUserDTO userDTO, @RequestParam(defaultValue = "false") boolean isPasswordHashed) throws UserExistsException {
        User user = userService.newUser(username, userDTO.getPassword(), isPasswordHashed, userDTO.getAuthorities());
        return convertUserToResponseUserDTO(userService.createUser(user));
    }

    @PutMapping("{username}")
    public ResponseUserDTO updateUser(@PathVariable String username, @RequestBody CreateUserDTO userDTO, @RequestParam(defaultValue = "false") boolean isPasswordHashed) throws UserMissingException {
        User user = userService.newUser(username, userDTO.getPassword(), isPasswordHashed, userDTO.getAuthorities());
        return convertUserToResponseUserDTO(userService.updateUser(user));
    }

    @DeleteMapping("{username}")
    public ResponseUserDTO deleteUser(@PathVariable String username) throws UserNotFoundException {
        return convertUserToResponseUserDTO(userService.deactivateUser(username));
    }

    private ResponseUserDTO convertUserToResponseUserDTO(User user) {
        return new ResponseUserDTO(user.getUsername(), user.getAuthorities().stream().map(Authority::getAuthorityEnum)
                .map(Authority.AuthorityEnum::name).toList(), user.isEnabled());
    }
}
