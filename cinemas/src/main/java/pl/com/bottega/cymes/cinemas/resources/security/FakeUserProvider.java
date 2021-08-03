package pl.com.bottega.cymes.cinemas.resources.security;

import org.springframework.stereotype.Component;

@Component
public class FakeUserProvider implements UserProvider {
    @Override
    public Long currentUserId() {
        return 10L;
    }
}
