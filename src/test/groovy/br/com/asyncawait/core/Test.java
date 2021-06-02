package br.com.asyncawait.core;

import br.com.asyncawait.core.models.Message;
import br.com.asyncawait.core.models.Pid;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
//            asyncAwait.sendMessage(pidProcessaString, new Message<>(Pid.newInstance(), "Jefferson"));
        }

        var async = asyncAwait.async(() -> "My name is Jefferson!");

        var result = async.await();

        System.out.println("Comprimento da string: " + result);

        testarComStream(asyncAwait);
    }

    private static void testarComStream(AsyncAwait asyncAwait) {
        var lista = new ArrayList<Integer>();

        for (int i = 2_000_000; i < 10_000_000; i++) {
            lista.add(i);
        }

        Predicate<Integer> isPrimo = numero -> {
            var maxDivisor = Math.sqrt(numero);

            for (int i = 2; i <= maxDivisor; i++) {
                if ((numero % i) == 0) {
                    return false;
                }
            }
            return true;
        };

        var inicio = LocalDateTime.now();

        var processos = lista.stream()
                .map(num -> {
                    var map = new HashMap<Async, Integer>(1);

                    map.put(asyncAwait.async(() -> isPrimo.test(num)), num);

                    return map;
                }).collect(Collectors.toList());

        processos.stream()
                .filter(objectObjectHashMap -> {
                    var async = objectObjectHashMap.keySet().stream().findFirst().orElse(null);

                    return (boolean) async.await();
                })
                .map(integerHashMap -> {
                    var key = integerHashMap.keySet().stream().findFirst().orElse(null);

                    return integerHashMap.get(key);
                }).forEach(System.out::println);

        var diff = Duration.between(inicio, LocalDateTime.now()).getSeconds();
        System.out.println("Durou: " + diff + " segundos!");

//        var inicio2 = LocalDateTime.now();
//
//        lista.stream()
//                .filter(isPrimo)
//                .forEach(System.out::println);
//
//        var diff2 = Duration.between(inicio2, LocalDateTime.now()).getSeconds();
//        System.out.println("Durou: " + diff2 + " segundos!");
    }
}
