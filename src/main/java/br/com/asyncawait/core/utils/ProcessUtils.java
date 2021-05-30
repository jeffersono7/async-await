package br.com.asyncawait.core.utils;

import br.com.asyncawait.core.models.Despatcher;
import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;
import br.com.asyncawait.core.models.Self;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProcessUtils implements Self {

    public final Despatcher despatcher;

    public static <T> Pid send(Pid pidTarget, T content) {
        var pidSender = Pid.newInstance();

        new Despatcher().send(pidTarget, new Message<T>(pidSender, content));

        return pidSender;
    }
}
