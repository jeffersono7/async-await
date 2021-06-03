package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Scheduler {

    private final Map<Pid, Process> processes = new ConcurrentHashMap<>();
    private ExecutorTask executorTask;

    Scheduler() {
        start();
    }

    void add(Process process) {
        processes.put(process.self(), process);

        this.executorTask.addProcessToRun(process);
    }

    void addMessageInQueue(Pid pid, Message<?> message) {
        var process = getProcessByPid(pid);
        process.addMessageInQueue(message);

        this.executorTask.addProcessToRun(process);
    }

    Process getProcessByPid(Pid pid) {
        return processes.get(pid);
    }

    int sizeQueue() {
        return processes.size();
    }

    void stop() {
        executorTask.stop();
    }

    private void start() {
        this.executorTask = ExecutorTask.getInstance();

        if (executorTask.isAlive()) {
            runAllProcesses();
        }
        // TODO quando executor morrer, é interessante subir outro
    }

    private void runAllProcesses() {
        processes.values().forEach(this.executorTask::addProcessToRun);
    }
}
