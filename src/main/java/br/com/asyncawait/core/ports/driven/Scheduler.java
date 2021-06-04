package br.com.asyncawait.core.ports.driven;

import br.com.asyncawait.core.Process;
import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;

public interface Scheduler {

    void add(Process process);

    void addMessageInQueue(Pid pid, Message<?> message);

    Process getProcessByPid(Pid pid);

    int sizeQueue();
}
