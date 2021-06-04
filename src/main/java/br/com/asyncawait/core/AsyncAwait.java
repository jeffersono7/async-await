package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;
import br.com.asyncawait.core.models.Self;
import br.com.asyncawait.core.ports.driven.Scheduler;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public final class AsyncAwait implements Self {

    private static AsyncAwait instance;

    private final List<Scheduler> schedulers = new ArrayList<>();
    private final Queue<Message> mailbox = new ConcurrentLinkedQueue();
    private Pid selfPid;

    private AsyncAwait() {
        var quantidadeThreadsSistema = 14; // TODO pegar inf do SO

        for (int i = 0; i < quantidadeThreadsSistema; i++) {
            this.schedulers.add(new SchedulerImpl());
        }

        var processBuilder = ProcessBuilder.getInstance(this);

        createSelfProcess(processBuilder);
    }

    public static synchronized AsyncAwait start() {
        if (instance == null) {
            instance = new AsyncAwait();
        }
        return instance;
    }

    // class methods

    @Override
    public Pid self() {
        return selfPid;
    }

    public <T> Async<T> async(Supplier<T> supplier) {
        var processBuilder = ProcessBuilder.getInstance(this);

        var async = Async.<T>getInstance();

        var asyncPid = processBuilder.spawn(((self, receiver, utils) -> {
            receiver.receive(Boolean.class, message -> {
                var content = supplier.get();

                async.accept(content);
            });
        }));

        this.sendMessage(asyncPid, new Message<>(Pid.newInstance(), Boolean.TRUE));

        return async;
    }

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

    @SneakyThrows
    public synchronized Message receiveMessage() {
        while (!hasMessage()) {
            this.wait();
        }
        return mailbox.poll();
    }

    public boolean hasMessage() {
        return !mailbox.isEmpty();
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

    private void createSelfProcess(ProcessBuilder processBuilder) {
        this.selfPid = processBuilder.spawn(
                ((self, receiver, utils) -> receiver.receive(Integer.class, message -> {
                    synchronized (this) {
                        mailbox.add(message);
                        this.notify();
                    }
                }))
        );
    }
}
