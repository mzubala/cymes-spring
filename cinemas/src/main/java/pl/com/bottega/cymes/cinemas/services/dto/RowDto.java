package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.Data;

import java.util.List;

@Data
public class RowDto {
    private Integer number;
    private List<RowElementDto> elements;
}
