package br.com.asyncawait.core.models;

import br.com.asyncawait.core.Receiver;
import br.com.asyncawait.core.utils.ProcessUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiConsumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Process implements Self {

    private final Queue<Object> filaProcessamento = new LinkedList<>();  // fila de mensagens
    private final Pid pid;
    private final BiConsumer<Receiver, ProcessUtils> run; // funcao deve receber mensagem

    public static Pid newInstance(BiConsumer<Receiver, ProcessUtils> run) {
        var pid = Pid.newInstance();

        new Process(pid, run);

        return pid;
    }
}
