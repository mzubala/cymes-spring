package pl.com.bottega.cymes.reservations.domain.model.model.application;

import pl.com.bottega.cymes.reservations.domain.model.model.model.Show;

import java.util.UUID;

public interface ShowProvider {

    Show get(UUID showId);
}
