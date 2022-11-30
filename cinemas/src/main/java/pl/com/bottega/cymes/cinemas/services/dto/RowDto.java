package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class RowDto {
    @NotNull
    private Integer number;
    @Valid
    @NotEmpty
    private List<RowElementDto> elements;
}
