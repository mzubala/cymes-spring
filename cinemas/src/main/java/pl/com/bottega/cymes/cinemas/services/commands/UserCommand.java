package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserCommand {
    @NotNull
    private Long userId;

    public UserCommand withUserId(Long userId) {
        setUserId(userId);
        return this;
    }
}
