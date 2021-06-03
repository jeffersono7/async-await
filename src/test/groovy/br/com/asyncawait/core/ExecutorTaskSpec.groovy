package br.com.asyncawait.core

import br.com.asyncawait.core.models.Message
import br.com.asyncawait.core.models.Pid
import br.com.asyncawait.core.models.ThreeConsumer
import br.com.asyncawait.core.utils.ProcessUtils
import spock.lang.Specification
import support.TestUtils

import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import java.util.function.BiConsumer

class ExecutorTaskSpec extends Specification implements TestUtils {

    private static final int UM_SEGUNDO = 1_000

    def "Deve retornar instância de executor e já iniciá-lo"() {
        given:
        def instancia = ExecutorTask.getInstance()

        expect:
        instancia != null
        instancia.threadExecutor.state in [Thread.State.NEW, Thread.State.RUNNABLE, Thread.State.WAITING]
    }

    def "Deve adicionar processo para executar e executá-lo"() {
        given:
        def runnableFuture = new FutureTask((anythings) -> { })
        def process = new Process(Pid.newInstance(), consumerMock(runnableFuture), despatcherMock())
        process.addMessageInQueue(new Message(Pid.newInstance(), "any things"))

        def instancia = ExecutorTask.getInstance()

        when:
        instancia.addProcessToRun(process)

        then:
        notThrown(runnableFuture.get(UM_SEGUNDO, TimeUnit.MILLISECONDS))
    }

    def "Deve parar executor"() {
        given:
        def instancia = ExecutorTask.getInstance()

        assert instancia.isAlive() == true

        when:
        instancia.stop()

        then:
        notThrown(waitUntil(() -> instancia.isAlive() == false, UM_SEGUNDO))
    }

    private ThreeConsumer<Pid, Receiver, ProcessUtils> consumerMock(FutureTask invokeFun) {
        (self, receiver, utils) -> {
            invokeFun.run()
        }
    }

    private BiConsumer<Pid, Message<?>> despatcherMock() {
        (pid, message) -> { }
    }
}
