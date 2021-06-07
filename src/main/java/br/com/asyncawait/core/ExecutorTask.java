package br.com.asyncawait.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutorTask implements Runnable {

    private final Queue<Process> processQueue = new ConcurrentLinkedQueue<>();
    private Thread threadExecutor;
    private boolean alive = true;

    public static ExecutorTask getInstance() {
        var executor = new ExecutorTask();

        var thread = new Thread(executor);
        executor.threadExecutor = thread;
        thread.start();

        return executor;
    }

    @Override
    public void run() {
        while (alive) {
            try {
                this.start();
            } catch (Exception e) {
                this.alive = false;
            }
        }
    }

    public synchronized void addProcessToRun(Process process) {
        processQueue.add(process);
        this.notifyAll();
    }

    public synchronized void stop() {
        processQueue.clear();
        alive = false;
        this.notifyAll();
    }

    public boolean isAlive() {
        return this.threadExecutor.isAlive();
    }

    @SneakyThrows
    private void start() {
        synchronized (this) {
            if (!processQueue.isEmpty()) {
                runProcess();
            }
            this.wait();
        }
    }

    private void runProcess() {
        while (!processQueue.isEmpty()) {
            var process = processQueue.poll();

            if (process == null) {
                return;
            }

            executeProcess(process);
        }
    }

    private void executeProcess(Process process) {
        try {
            process.processMessage();
        } catch (Exception e) {
        }
    }
}
