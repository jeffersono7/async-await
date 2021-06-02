package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public final class AsyncAwait {

    private static AsyncAwait instance;

    private final List<Scheduler> schedulers = new ArrayList<>();

    private AsyncAwait() {
        var quantidadeThreadsSistema = 14; // TODO pegar inf do SO

        for (int i = 0; i < quantidadeThreadsSistema; i++) {
            this.schedulers.add(new Scheduler());
        }
    }

    public static synchronized AsyncAwait start() {
        if (instance == null) {
            instance = new AsyncAwait();
        }
        return instance;
    }

    // class methods

    public <T> Async<T> async(Supplier<T> supplier) {
        var processBuilder = ProcessBuilder.getInstance(this);

        var async = Async.<T>getInstance();

        var asyncPid = processBuilder.createProcess(((self, receiver, utils) -> {
            receiver.receive(Boolean.class, message -> {
                var content = supplier.get();

                async.accept(content);
            });
        }));

        this.sendMessage(asyncPid, new Message<>(Pid.newInstance(), Boolean.TRUE));

        return async;
    }

//    public <T> Pid async(Pid target, Message<T> message) {
//        sendMessage(target, message);
//    }

    public void sendMessage(Pid pid, Message<?> message) {
        var schedulerIterator = schedulers.iterator();

        while (schedulerIterator.hasNext()) {
            var scheduler = schedulerIterator.next();
            var process = scheduler.getProcessByPid(pid);

            if (process != null) {
                scheduler.addMessageInQueue(pid, message);
            }
        }
    }

    void addProcess(Process process) {
        var betterScheduler = getBetterScheduler();

        betterScheduler.add(process);
    }

    private Scheduler getBetterScheduler() {
        return this.schedulers.stream()
                .sorted(Comparator.comparing(Scheduler::sizeQueue))
                .findFirst()
                .orElseThrow();
    }
}
