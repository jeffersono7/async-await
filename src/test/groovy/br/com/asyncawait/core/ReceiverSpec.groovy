package br.com.asyncawait.core

import br.com.asyncawait.core.models.Message
import br.com.asyncawait.core.models.Pid
import spock.lang.Specification

class ReceiverSpec extends Specification {

    private Receiver receiver;

    def "Deve cadastrar receivers"() {
        given:
        receiver = new Receiver();

        when:
        var mesmaInstancia = receiver.receive(String.class, string -> {})

        then:
        mesmaInstancia == receiver
        receiver.receivers().size() == 1
    }

    def "Consumer incluido no receiver deve ser chamado"() {
        given:
        def expectedMessage = new Message(Pid.newInstance(), "working")
        receiver = new Receiver()

        when:
        Message result

        receiver.receive(String.class, message -> {
            result = message
        });
        receiver.receivers().forEach((tClass, consumer) -> consumer.accept(expectedMessage))

        then:
        expectedMessage == result
        expectedMessage.content == result.content
    }

    def "Deve setar catchException"() {
        receiver = new Receiver()
        def exception = null

        when:
        receiver.catchException(e -> exception = e)
        receiver.getConsumerException().accept(new Exception("ok!"))

        then:
        exception != null
    }

    def "Quando não tiver setado um consumerException, deve retornar um default sem ação"() {
        receiver = new Receiver()

        when:
        def consumerException = receiver.getConsumerException()

        then:
        consumerException != null
        consumerException.accept(new Exception())
    }
}
