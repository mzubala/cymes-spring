package pl.com.bottega.cymes.cinemas.services.commands;

import lombok.Data;

@Data
public class UserCommand {
    private Long userId;

    public UserCommand withUserId(Long userId) {
        setUserId(userId);
        return this;
    }
}
