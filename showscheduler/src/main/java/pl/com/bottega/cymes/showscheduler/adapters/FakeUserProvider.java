package pl.com.bottega.cymes.showscheduler.adapters;

public class FakeUserProvider implements UserProvider {
    @Override
    public Long currentUserId() {
        return 10L;
    }
}
