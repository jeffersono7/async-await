package br.com.asyncawait.core.utils;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class ProcessUtils {

    public final BiConsumer<Pid, Message<?>> despatcher;
}
