package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.dataaccess.model.RowElementKind;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class RowElementDto {

    @NotNull
    @Min(0)
    private Integer index;

    @NotNull
    @Min(1)
    private Integer number;

    @NotNull
    private RowElementKind kind;
}
