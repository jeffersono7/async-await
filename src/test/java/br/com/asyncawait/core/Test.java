package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;

public class Test {

    public static void main(String... args) {
        var asyncAwait = AsyncAwait.start();
        var processBuilder = ProcessBuilder.getInstance(asyncAwait);

        Pid pidGetSizeString = processBuilder.createProcess((self, receiver, utils) -> {
            receiver.receive(String.class, message -> {
                var content = message.getContent();

                System.out.println("Comprimento da string: " + content);

                utils.despatcher.accept(message.getSender(), new Message<>(self, content.length()));
            });
        });

        Pid pidProcessaString = processBuilder.createProcess((self, receiver, utils) -> {
            receiver.receive(String.class, message -> { // TODO ver para message ser um deep copy
                var nome = message.getContent();

                utils.despatcher.accept(pidGetSizeString, new Message<>(message.getSender(), "My name is " + nome + "!"));
            });
        });

        for (int i = 0; i < 10_000_000; i++) {
            asyncAwait.sendMessage(pidProcessaString, new Message<>(Pid.newInstance(), "Jefferson"));
        }
//        Pid meuPid = algumaCoisa.async(pidProcessaString, "Jefferson");
//        var result = algumaCoisa.await(meuPid);
//        System.out.println("Comprimento da string: " + result);
    }
}
