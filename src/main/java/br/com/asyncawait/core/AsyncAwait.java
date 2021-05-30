package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Pid;

import java.util.ArrayList;
import java.util.List;

public class AsyncAwait {

    private static boolean inicializado = false;

    private final List<Scheduler> schedulers = new ArrayList<>();

    private AsyncAwait() {
        inicializado = true;

        var quantidadeThreadsSistema = 14; // TODO pegar inf do SO

        for (int i = 0; i < quantidadeThreadsSistema; i++) {
            this.schedulers.add(new Scheduler());
        }
    }

    public static synchronized void start() {
        if (!inicializado) {
            new AsyncAwait();
        }
    }

    public static <T> T await(Pid pid) {
        return null; // TODO impl
    }
}
