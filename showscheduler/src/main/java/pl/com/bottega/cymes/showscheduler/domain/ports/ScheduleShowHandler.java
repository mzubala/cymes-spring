package pl.com.bottega.cymes.showscheduler.domain.ports;

import pl.com.bottega.cymes.showscheduler.domain.ScheduleShowCommand;

public interface ScheduleShowHandler {

    void handle(ScheduleShowCommand command);

}
