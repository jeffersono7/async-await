package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Despatcher;
import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;
import br.com.asyncawait.core.models.Self;
import br.com.asyncawait.core.utils.ProcessUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Process implements Self {

    private final Queue<Message> mailBox = new ConcurrentLinkedQueue<>();  // fila de mensagens
    private final Pid pid;
    private final BiConsumer<Receiver, ProcessUtils> run; // funcao deve receber mensagem

    public static Pid newInstance(BiConsumer<Receiver, ProcessUtils> run) {
        var pid = Pid.newInstance();

        new Process(pid, run);

        return pid;
    }

    void processMessage() {
        var message = mailBox.poll();

        if (message == null) {
            return;
        }

        var receiver = new Receiver();

        run.accept(receiver, new ProcessUtils(new Despatcher()));

        var contentClass = message.getContent().getClass();

        receiver.receivers().getOrDefault(contentClass, m -> {}).accept(message);
    }
}
