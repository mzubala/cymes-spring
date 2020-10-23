package pl.com.bottega.cymes.reservations.domain.application;

import pl.com.bottega.cymes.reservations.domain.model.Show;

import java.util.UUID;

public interface ShowProvider {

    Show get(UUID showId);
}
