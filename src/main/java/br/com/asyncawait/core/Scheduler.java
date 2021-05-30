package br.com.asyncawait.core;

import java.util.LinkedList;
import java.util.Queue;

// TODO ver forma de indexar o pid, necessário para encaminhar uma mensagem
public final class Scheduler {

    private Queue<Process> processQueue = new LinkedList<>();

    void add(Process process) {
        processQueue.add(process);
    }

    int sizeQueue() {
        return processQueue.size();
    }
}
