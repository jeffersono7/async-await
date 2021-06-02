package br.com.asyncawait.core

import spock.lang.Specification

class AsyncSpec extends Specification {

    private static final String MOCK_CONTENT = "Mock content";


    def "Deve retornar uma instancia de Async"() {
        when:
        def instancia = Async.getInstance()

        then:
        null != instancia
    }

    def "Deve esperar receber um conteúdo, para então retorná-lo"() {
        def async = Async.getInstance()

        when:
        setContentWithDelay(async, 200)

        def result = async.await()

        then:
        MOCK_CONTENT == result
    }

    def "Quando já houver conteúdo deve retornar de imediato"() {
        def async = Async.getInstance()

        when:
        async.accept(MOCK_CONTENT)

        def result = async.await()

        then:
        MOCK_CONTENT == result
    }

    private void setContentWithDelay(Async async, int delay) {
        new Thread(() -> {
            Thread.sleep(delay)
            async.accept(MOCK_CONTENT)
        }).start();
    }
}
