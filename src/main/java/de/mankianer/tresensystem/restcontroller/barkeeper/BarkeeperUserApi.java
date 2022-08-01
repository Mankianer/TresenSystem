package de.mankianer.tresensystem.restcontroller.barkeeper;

import de.mankianer.tresensystem.exeptions.UserNotFoundException;
import de.mankianer.tresensystem.restcontroller.dto.ResponseUserDTO;
import de.mankianer.tresensystem.restcontroller.dto.UserDTO;
import de.mankianer.tresensystem.security.UserRepository;
import de.mankianer.tresensystem.security.entities.Authority;
import de.mankianer.tresensystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bar/user/")
public class BarkeeperUserApi {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;


    public BarkeeperUserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDTO> getUser() {
        return userRepository.findAllByIsActiveAndAuthorities_AuthorityEnum(true, Authority.AuthorityEnum.USER).stream().map(UserDTO::new).collect(Collectors.toUnmodifiableList());
    }

    @GetMapping("{username}")
    public ResponseUserDTO getUser(@PathVariable String username) throws UserNotFoundException {
        return userService.convertUserToResponseUserDTO(userService.findUser(username));
    }
}
