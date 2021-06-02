package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Receiver {

    private final Map<Class<?>, Consumer<Message>> receiveMatchers = new HashMap<>();

    public <T> Receiver receive(Class<T> tClass, Consumer<Message<T>> run) {
        Consumer<Message> genericRun = message -> {
            run.accept((Message<T>) message);
        };

        this.receiveMatchers.put(tClass, genericRun);

        return this;
    }

    Map<Class<?>, Consumer<Message>> receivers() {
        return this.receiveMatchers;
    }
}
