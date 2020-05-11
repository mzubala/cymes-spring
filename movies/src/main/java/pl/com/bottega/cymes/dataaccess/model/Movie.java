package pl.com.bottega.cymes.dataaccess.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Movie {
    private Long id;
    private String title;
    private Star director;
    private List<Star> actors;
    private Set<Genre> generes;
    private Integer durationMinutes;
}
