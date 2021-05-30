package br.com.asyncawait.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Pid {
    private final UUID id;

    public static Pid newInstance() {
        return new Pid(UUID.randomUUID());
    }
}
