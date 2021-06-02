package br.com.asyncawait.core;

import lombok.SneakyThrows;

public class Async<T> {

    private T content;

    public static <T> Async<T> getInstance() {
        return new Async<>();
    }

    @SneakyThrows
    public synchronized T await() {
        while (content == null) {
            this.wait();
        }
        return content;
    }

    synchronized void accept(T content) {
        this.content = content;
        this.notify();
    }
}
