package br.com.asyncawait.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message<T> {
    private final Pid sender;
    private final T content;
}
