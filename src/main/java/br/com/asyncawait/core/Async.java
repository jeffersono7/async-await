package br.com.asyncawait.core;

import lombok.SneakyThrows;

public class Async<T> {

    private T content;
    private Exception exception;

    public static <T> Async<T> getInstance() {
        return new Async<>();
    }

    @SneakyThrows
    public synchronized T await() {
        while (content == null && exception == null) {
            this.wait();
        }

        if (content != null) {
            return content;
        }

        throw exception;
    }

    synchronized void accept(T content) {
        this.content = content;
        this.notify();
    }

    synchronized void catchException(Exception e) {
        this.exception = e;
        this.notify();
    }
}
