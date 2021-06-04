package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid
import spock.lang.Specification;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class AsyncAwaitIT extends Specification {

    def "Deve fazer troca de mensagens entre processos e executá-los"() {
        given:
        def asyncAwait = AsyncAwait.start();
        def processBuilder = ProcessBuilder.getInstance(asyncAwait);

        def pidGetSizeString = processBuilder.spawn((self, receiver, utils) -> {
            receiver.receive(String.class, message -> {
                var content = message.getContent();

                System.out.println("Comprimento da string: " + content);

                utils.despatcher.accept(message.getSender(), new Message<>(self, content.length()));
            });
        });
        def pidProcessaString = processBuilder.spawn((self, receiver, utils) -> {
            receiver.receive(String.class, message -> {
                var nome = message.getContent();

                utils.despatcher.accept(pidGetSizeString, new Message<>(message.getSender(), "My name is " + nome + "!"));
            });
        });

        when:
        def myPid = asyncAwait.self()
        asyncAwait.sendMessage(pidProcessaString, new Message<>(myPid, "Jefferson"));
        asyncAwait.sendMessage(pidProcessaString, new Message<>(myPid, "ok"));

        then:
        def result1 = asyncAwait.receiveMessage()
        def result2 = asyncAwait.receiveMessage()

        result1.content in [21, 14]
        result2.content in [21, 14]
    }
}
