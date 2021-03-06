package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;
import br.com.asyncawait.core.models.Self;
import br.com.asyncawait.core.models.ThreeConsumer;
import br.com.asyncawait.core.utils.ProcessUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Process implements Self {

    private final Queue<Message> mailBox = new ConcurrentLinkedQueue<>();
    private final Pid pid;
    private final ThreeConsumer<Pid, Receiver, ProcessUtils> run;
    private final BiConsumer<Pid, Message<?>> despatcher;
    private boolean alive = true;

    @Getter
    private Exception exception;

    // TODO problema de poder receber o pid source, também complica na questão de não ter pattern matching
    //  para poder chamar o "callback" para ir retornando no fluxo
    public static Pid newInstance(AsyncAwait asyncAwait, ThreeConsumer<Pid, Receiver, ProcessUtils> run) {
        var pid = Pid.newInstance();

        var process = new Process(pid, run, asyncAwait::sendMessage);

        asyncAwait.addProcess(process);

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

        var contentClass = message.getContent().getClass(); // TODO problema de classe superior ou até mesmo interface

        try {
            receiver.receivers().getOrDefault(contentClass, consumerImpl()).accept(message);
        } catch (Exception e) {
            receiver.getConsumerException().accept(e);

            this.breakProcess(e);

            throw e;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    void breakProcess(Exception e) {
        this.alive = false;
        this.exception = e;
    }

    private Consumer<Message> consumerImpl() {
        return m -> {
        };
    }
}
