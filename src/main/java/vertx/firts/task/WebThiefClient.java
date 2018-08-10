package vertx.firts.task;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.buffer.Buffer;

import java.util.concurrent.atomic.AtomicReference;

public class WebThiefClient extends AbstractVerticle {
    @Override
    public void start(Future<Void> fut) {
        WebClient webClient = WebClient.create(vertx);

        Integer port = 443;
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("vertx.firts.task.WebThiefClient", message -> {
            System.out.printf("I have received a message: " + message.body());

            String siteUrl = message.body().toString();
            webClient
                    .get(port, siteUrl, "")
                    .ssl(true)
                    .send(ar -> {
                        if (ar.succeeded()) {
                            //Obtain response
                            HttpResponse<Buffer> response = ar.result();
                            System.out.println("Received response with status code" + response.statusCode());
                            System.out.println(response.bodyAsString());
                            eventBus.send("vertx.firts.task.WebThiefServer", response.bodyAsBuffer());
                        } else {
                            System.out.println("Something went wrong " + ar.cause());
                        }
                    });
        });


    }

}
