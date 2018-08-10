package vertx.firts.task;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main2 {
    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions().setClustered(true), vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                vertxAsyncResult.result().deployVerticle("vertx.firts.task.WebThiefClient", res ->
                {
                    if (res.succeeded()) {
                        System.out.println("Deployment id is: " + res.result());
                    } else {
                        System.out.println("Deployment failed!" + res.cause());
                    }

                });
            }
        });
    }

}