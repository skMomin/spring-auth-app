package com.momin.springauthapp.endpoint;

import com.momin.springauthapp.persistence.repository.UserRepository;
import com.momin.springauthapp.user.UserDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userRepository.findAll();
    }
}
