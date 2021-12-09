package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;
import br.com.asyncawait.core.ports.driven.Scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SchedulerImpl implements Scheduler {

    private final Map<Pid, Process> processes = new ConcurrentHashMap<>();
    private ExecutorTask executorTask;

    SchedulerImpl() {
        start();
    }

    @Override
    public void add(Process process) {
        processes.put(process.self(), process);

        verifyExecutorTaskAlive();
        this.executorTask.addProcessToRun(process);
    }

    @Override
    public void addMessageInQueue(Pid pid, Message<?> message) {
        var process = getProcessByPid(pid);
        process.addMessageInQueue(message);

        verifyExecutorTaskAlive();
        this.executorTask.addProcessToRun(process);
    }

    @Override
    public Process getProcessByPid(Pid pid) {
        return processes.get(pid);
    }

    @Override
    public int sizeQueue() {
        return processes.size();
    }

    void stop() {
        executorTask.stop();
    }

    private void start() {
        this.executorTask = ExecutorTask.getInstance();

        runAllProcesses();
    }

    private void runAllProcesses() {
        verifyExecutorTaskAlive();

        processes.values()
                .stream()
                .filter(Process::isAlive)
                .forEach(this.executorTask::addProcessToRun);
    }

    private void verifyExecutorTaskAlive() {
        if (!executorTask.isAlive()) {
            executorTask = ExecutorTask.getInstance();
        }
    }
}
