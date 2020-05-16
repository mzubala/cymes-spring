package pl.com.bottega.cymes.showscheduler.domain;

public interface SuspensionChecker {
    boolean isSuspended(Long cinemaId, Long cinemaHallId);
}
