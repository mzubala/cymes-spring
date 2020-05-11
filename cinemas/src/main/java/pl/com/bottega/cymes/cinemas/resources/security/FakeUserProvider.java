package pl.com.bottega.cymes.cinemas.resources.security;

public class FakeUserProvider implements UserProvider {
    @Override
    public Long currentUserId() {
        return 10L;
    }
}
