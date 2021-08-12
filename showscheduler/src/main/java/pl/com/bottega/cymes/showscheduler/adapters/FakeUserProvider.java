package pl.com.bottega.cymes.showscheduler.adapters;

import org.springframework.stereotype.Component;

@Component
public class FakeUserProvider implements UserProvider {
    @Override
    public Long currentUserId() {
        return 10L;
    }
}
