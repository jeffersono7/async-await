package br.com.asyncawait.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutorTask implements Runnable {

    private Queue<Process> processQueue = new ConcurrentLinkedQueue<>();
    private Thread threadExecutor;

    public static ExecutorTask getInstance() {
        var executor = new ExecutorTask();

        var thread = new Thread(executor);
        executor.threadExecutor = thread;
        thread.start();

        return executor;
    }

    @Override
    public void run() {
        while (true) {
            this.start();
        }
    }

    public synchronized void addProcessToRun(Process process) {
        processQueue.add(process);
        this.notifyAll();
    }

    public synchronized void stop() {
        processQueue.clear();
        this.notify();
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
        var process = processQueue.poll();

        if (process != null) {
            process.processMessage();
        }
    }
}
