package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Pid;
import br.com.asyncawait.core.models.ThreeConsumer;
import br.com.asyncawait.core.utils.ProcessUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProcessBuilder {

    private final AsyncAwait asyncAwait;

    public static ProcessBuilder getInstance(AsyncAwait asyncAwait) {
        return new ProcessBuilder(asyncAwait);
    }

    public Pid createProcess(ThreeConsumer<Pid, Receiver, ProcessUtils> run) {
        return Process.newInstance(asyncAwait, run);
    }
}

