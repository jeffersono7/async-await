package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO ver forma de indexar o pid, necessário para encaminhar uma mensagem
public final class Scheduler {

    private Map<Pid, Process> processes = new ConcurrentHashMap<>();
    private ExecutorTask executorTask;

    Scheduler() {
        start();
    }

    void add(Process process) {
        processes.put(process.self(), process);
    }

    void addMessageInQueue(Pid pid, Message<?> message) {
        getProcessByPid(pid).addMessageInQueue(message);

        this.runAllProcesses();
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

        runAllProcesses();
    }

    private void runAllProcesses() {
        processes.values().forEach(this.executorTask::addProcessToRun);
    }
}
