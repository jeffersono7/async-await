package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Process;
import br.com.asyncawait.core.models.Pid;
import br.com.asyncawait.core.utils.ProcessUtils;

public class Test {

    public static void main(String ...args) {
        AsyncAwait.start();

        Pid pidGetSizeString = Process.newInstance((receiver, utils) -> {
            receiver.receive(String.class, message -> {
               var content = message.getContent();

               utils.despatcher.send(message.getSender(), new Message<>(utils.self(), content.length()));
            });
        });

        Pid pidProcessaString = Process.newInstance((receiver, utils) -> {
            receiver.receive(Void.class, i -> {
                utils.despatcher.send(pidGetSizeString, new Message<>(utils.self(), "My name is Jefferson!"));

            });
        });

        Pid meuPid = ProcessUtils.send(pidProcessaString, null);
        var result = AsyncAwait.<Integer>await(meuPid);
        System.out.println("Comprimento da string: " + result);
    }
}
