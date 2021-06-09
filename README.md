# Async/Await

[![AsyncAwait](https://circleci.com/gh/jeffersono7/async-await.svg?style=svg)](https://app.circleci.com/pipelines/github/jeffersono7/async-await)

Library for implementing actors for parallel and asynchronous processing inspired by Elixir / Erlang OTP

## Usage

Start the AsyncAwait with:

```java
import br.com.asyncawait.core.AsyncAwait;

public class Main {

    public static void main(String ...args) {
        var asyncAwait = AsyncAwait.start();
    }
}
```

If you are using the Spring Framework, you can create a configuration to call AsyncAwait.start() 
and make the returned instance available in your application.

You can use it with async/await, for example:

```java
import br.com.asyncawait.core.AsyncAwait;

public class Main {

    public static void main(String ...args) {
        var asyncAwait = AsyncAwait.start();
        
        var async1 = asyncAwait.async(() -> "an example!");
        var async2 = asyncAwait.async(() -> "a second example!");
        
        async2.await().equals("a second example!"); // true
        async1.await().equals("an example!"); // true
    }
}
```

The order of execution does not matter, so you can parallelize tasks.

You can use it with processes, for example:

```java
import br.com.asyncawait.core.AsyncAwait;
import br.com.asyncawait.core.ProcessBuilder;
import br.com.asyncawait.core.models.Message;

import java.util.List;

public class Main {

  public static void main(String... args) {
    var asyncAwait = AsyncAwait.start();
    var processBuilder = ProcessBuilder.getInstance(asyncAwait);

    var processIncrement = processBuilder.spawn((self, receiver, utils) -> {
      receiver.receive(Integer.class, message -> {
        var result = message.getContent() + 1;

        utils.despatcher.accept(message.getSender(), new Message<>(self, result));
      });
    });

    asyncAwait.sendMessage(processIncrement, new Message<>(asyncAwait.self(), 1));
    asyncAwait.sendMessage(processIncrement, new Message<>(asyncAwait.self(), 2));
    asyncAwait.sendMessage(processIncrement, new Message<>(asyncAwait.self(), 3));

    List.<Integer>of(
            asyncAwait.receiveMessage().getContent(),
            asyncAwait.receiveMessage().getContent(),
            asyncAwait.receiveMessage().getContent()
    ); // contains 2,3,4 in any order.
  }
}
```

You can create any process that does something, then you can send messages to it,
for each message it will execute the defined function.

Each process created will be allocated in a scheduler and consequently in a thread.
The scheduler processes one message from each process it owns at a time, giving your processes processing time.

### Note

- We don't use mutex/lock here, to guarantee execution order, our goal is performance, so concurrency issues or simultaneous access in
  objects can happen.
- We recommend using functional programming to avoid inconsistency issues.
