package pl.com.bottega.cymes.movies.model;

import lombok.Data;

@Data
public class Star {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
}
