package pl.com.bottega.cymes.reservations.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CinemaHallExample {

    private final Map<Integer, Integer> seatsMap = new HashMap<>();

    public static CinemaHallExample aCinemaHall() {
        return new CinemaHallExample();
    }

    public CinemaHallExample withSeatsInRow(Integer rowNumber, Integer seatsCount) {
        seatsMap.put(rowNumber, seatsCount);
        return this;
    }

    public CinemaHall build() {
        var example = this;
        if(seatsMap.size() == 0) {
            example = withSeatsInRow(1, 50).withSeatsInRow(2, 50);
        }
        return new CinemaHall(example.generateSeats());
    }

    private Set<Seat> generateSeats() {
        return seatsMap.entrySet().stream()
            .flatMap(entry -> IntStream.range(1, entry.getValue() + 1)
                .mapToObj(seatNo -> new Seat(entry.getKey(), seatNo)))
            .collect(Collectors.toSet());
    }
}
