package br.com.asyncawait.core

import spock.lang.Specification
import support.TestUtils

class AsyncAwaitSpec extends Specification implements TestUtils {

    def "Deve iniciar AsyncAwait com seus schedulers e retornar instância"() {
        when:
        def asyncAwait = AsyncAwait.start();

        then:
        asyncAwait != null
        asyncAwait.schedulers.size() > 0
    }

    def "Deve retornar mesma instância sempre que invocado método estático start"() {
        when:
        def asyncAwait = AsyncAwait.start()

        then:
        asyncAwait == AsyncAwait.start()
    }

    def "Deve executar função assincrona"() {
        given:
        def expected = "Jefferson"
        def asyncAwait = AsyncAwait.start()

        when:
        def asyncInstance = asyncAwait.async(supplierObjectWithDelay(expected, 200))

        then:
        asyncInstance.await() == expected
    }

    def "Ao executar processo que lance um exception, não deve quebrar infra do async/await"() {
        given:
        def asyncAwait = AsyncAwait.start()
        def expected = "Esse funciona!"

        def asyncInstanceBroke = asyncAwait.async(() -> {
            throw new RuntimeException("Broke")
        })
        def asyncInstance = asyncAwait.async(() -> expected)

        when:
        def result = asyncInstance.await()
        asyncInstanceBroke.await() // Esse vem depois porque vai lançar excessão

        then:
        result == expected
        thrown(RuntimeException)
    }
}
