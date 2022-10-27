package pl.com.bottega.ecom.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;

    public User getById(UUID userId) {
        return userRepository.getById(userId);
    }
}
