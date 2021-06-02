package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;
import br.com.asyncawait.core.models.Self;
import br.com.asyncawait.core.models.ThreeConsumer;
import br.com.asyncawait.core.utils.ProcessUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Process implements Self {

    private final Queue<Message> mailBox = new ConcurrentLinkedQueue<>();  // fila de mensagens
    private final Pid pid;
    private final ThreeConsumer<Pid, Receiver, ProcessUtils> run;
    private final BiConsumer<Pid, Message<?>> despatcher;

    public static Pid newInstance(AsyncAwait asyncAwait, ThreeConsumer<Pid, Receiver, ProcessUtils> run) {
        var pid = Pid.newInstance();

        var process = new Process(pid, run, asyncAwait::sendMessage);

        AsyncAwait.start().addProcess(process);

        return pid;
    }

    @Override
    public Pid self() {
        return pid;
    }

    void addMessageInQueue(Message message) {
        this.mailBox.add(message);
    }

    void processMessage() {
        var message = mailBox.poll();

        if (message == null) {
            return;
        }

        var receiver = new Receiver();
        var processUtils = new ProcessUtils(despatcher);

        // somente para registrar os matchers no receiver
        run.accept(pid, receiver, processUtils);

        var contentClass = message.getContent().getClass();

        receiver.receivers().getOrDefault(contentClass, consumerImpl()).accept(message);
    }

    private Consumer<Message> consumerImpl() {
        return m -> {
        };
    }
}
