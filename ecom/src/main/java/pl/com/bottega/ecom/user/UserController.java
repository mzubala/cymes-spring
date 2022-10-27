package pl.com.bottega.ecom.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserRepository userRepository;

    @PostMapping
    RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        var user = new User(UUID.randomUUID(), request.email, request.password);
        userRepository.save(user);
        return new RegisterResponse(user.getId());
    }

    @Value
    static class RegisterResponse {
        UUID userId;
    }

    @Data
    static class RegisterRequest {
        @Email
        String email;
        @NotBlank
        @Size(min = 6)
        String password;
    }
}
