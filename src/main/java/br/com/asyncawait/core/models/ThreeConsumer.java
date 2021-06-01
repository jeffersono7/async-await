package br.com.asyncawait.core.models;

@FunctionalInterface
public interface ThreeConsumer<Param1, Param2, Param3> {

    void accept(Param1 p1, Param2 param2, Param3 p3);
}
