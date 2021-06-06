package br.com.asyncawait.core

import br.com.asyncawait.core.models.Message
import br.com.asyncawait.core.models.Pid
import spock.lang.Specification
import support.TestUtils

class SchedulerImplSpec extends Specification implements TestUtils {

    def asyncAwait = AsyncAwait.start()

    def "Ao instanciar um scheduler, deve subir um executor junto"() {
        when:
        def scheduler = new SchedulerImpl()

        then:
        scheduler.executorTask != null
    }

    def "Deve adicionar processo para executar"() {
        given:
        def process = new Process(Pid.newInstance(), (self, receiver, utils) -> { }, (pid, message) -> { })
        def scheduler = new SchedulerImpl()

        when:
        scheduler.add(process)

        then:
        scheduler.sizeQueue() == 1
    }

    def "Deve parar Scheduler, também parando seu ExecutorTask"() {
        given:
        def scheduler = new SchedulerImpl()

        when:
        scheduler.stop()

        then:
        Thread.sleep(200)
        scheduler.executorTask.alive == false
    }
}
