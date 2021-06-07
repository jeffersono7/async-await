package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Receiver {

    private final Map<Class<?>, Consumer<Message>> receiveMatchers = new HashMap<>();
    private Consumer<Exception> consumerException;

    public <T> Receiver receive(Class<T> tClass, Consumer<Message<T>> run) {
        Consumer<Message> genericRun = message -> {
            run.accept((Message<T>) message);
        };

        this.receiveMatchers.put(tClass, genericRun);

        return this;
    }

    public void catchException(Consumer<Exception> catchException) {
        this.consumerException = catchException;
    }

    Map<Class<?>, Consumer<Message>> receivers() {
        return this.receiveMatchers;
    }

    Consumer<Exception> getConsumerException() {
        if (consumerException != null) {
            return consumerException;
        }
        return e -> {
        };
    }
}
