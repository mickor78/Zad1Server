package vertx.firts.task;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import sun.security.provider.certpath.Vertex;

public class Main1 {
    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle("vertx.firts.task.WebThiefServer", res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!" + res.cause());
            }
        });
    }
}
