package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RowDto {
    @NotNull
    private Integer number;
    @Valid
    @NotEmpty
    private List<RowElementDto> elements;
}
