package support

import java.util.concurrent.TimeoutException
import java.util.function.Supplier

interface TestUtils {

    default void waitSupplierUntil(Supplier<Boolean> test, int timeUntil) {
        int countTime = 0

        while (true) {
            synchronized (this) {
                if (test.get()) {
                    return
                }
                sleep(100)
                countTime += 100

                if (countTime >= timeUntil) {
                    throw new TimeoutException("Tempo excedido para condição ser verdadeira!")
                }
            }
        }
    }

    default <T> Supplier<T> supplierObjectWithDelay(T t, int delay) {
        () -> {
            Thread.sleep(delay)
            t
        };
    }
}
