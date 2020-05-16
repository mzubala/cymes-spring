package pl.com.bottega.cymes.showscheduler.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Random;

@With
@AllArgsConstructor
@NoArgsConstructor
class MovieExample {
    private Long id = new Random().nextLong();
    private Integer duration = new Random().nextInt(140);

    Movie toMovie() {
        return new Movie(id, duration);
    }
}
