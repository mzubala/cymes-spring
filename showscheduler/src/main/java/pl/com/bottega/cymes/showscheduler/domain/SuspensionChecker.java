package pl.com.bottega.cymes.showscheduler.domain;

public interface SuspensionChecker {
    boolean anySuspensionsAtTimeOf(Show show);
}
