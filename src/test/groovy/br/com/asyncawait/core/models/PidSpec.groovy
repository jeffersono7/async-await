package br.com.asyncawait.core.models


import spock.lang.Specification

class PidSpec extends Specification {

    def "Deve retornar uma instância de Pid com id"() {
        when:
        def instancia = Pid.newInstance()

        then:
        null != instancia
        null != instancia.id
    }
}
