package pl.com.bottega.cymes.showscheduler.adapters;

import pl.com.bottega.cymes.showscheduler.domain.Show;
import pl.com.bottega.cymes.showscheduler.domain.SuspensionChecker;

public class SuspensionCheckerAdapter implements SuspensionChecker {

    @Override
    public boolean anySuspensionsAtTimeOf(Show show) {
        return false;
    }
}
