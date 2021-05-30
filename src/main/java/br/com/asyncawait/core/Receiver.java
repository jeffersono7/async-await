package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;

import java.util.function.Consumer;

public class Receiver {

    public <T> Receiver receive(Class<T> tClass, Consumer<Message<T>> run) {

        return this;
    }
}
