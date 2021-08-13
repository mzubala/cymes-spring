package pl.com.bottega.cymes.showscheduler.domain.ports;

import pl.com.bottega.cymes.showscheduler.domain.Show;

public interface SuspensionChecker {
    boolean anySuspensionsAtTimeOf(Show show);
}
