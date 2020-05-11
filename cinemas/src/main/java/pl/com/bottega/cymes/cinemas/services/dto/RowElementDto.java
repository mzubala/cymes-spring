package pl.com.bottega.cymes.cinemas.services.dto;

import lombok.Data;
import pl.com.bottega.cymes.cinemas.dataaccess.model.RowElementKind;

@Data
public class RowElementDto {
    private Integer index;
    private Integer number;
    private RowElementKind kind;
}
