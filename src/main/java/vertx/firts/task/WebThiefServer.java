package vertx.firts.task;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class WebThiefServer extends AbstractVerticle {


    @Override
    public void start(Future<Void> fut){
        WebClient client = WebClient.create(vertx);
        EventBus eventBus = vertx.eventBus();

        //Create a router object
        Router router = Router.router(vertx);

        Route route = router.route(HttpMethod.GET,"/:siteUrl");

        route.handler(routingContext -> {

            String siteUrl = routingContext.request().getParam("siteUrl");
            HttpServerResponse response = routingContext.response();


            eventBus.send("vertx.firts.task.WebThiefClient",siteUrl);
            eventBus.consumer("vertx.firts.task.WebThiefServer",message -> {
                System.out.println("jestem zajebisty");
                System.out.println(message.body());
                response
                        .putHeader("content-type","text/html")
                        .end(message.body().toString());
            });


            /*
            client
                    .get(443, siteUrl,"")
                    .ssl(true)
                    .send(ar -> {
                        if (ar.succeeded()) {
                            // Obtain response
                            HttpResponse<Buffer> response1 = ar.result();

                            response
                                    .putHeader("content-type","text/html")
                                    .end(response1.bodyAsString());

                            System.out.println("Received response with status code" + response1.statusCode());
                            System.out.println("Received response body" + response1.bodyAsString());
                        } else {
                            System.out.println("Something went wrong " + ar.cause().getMessage());
                        }
                    });

            client
                    .get(80, siteUrl,"")
                    .send(ar -> {
                        if (ar.succeeded()) {
                            // Obtain response
                            HttpResponse<Buffer> response1 = ar.result();

                            response
                                    .putHeader("content-type","text/html")
                                    .end(response1.bodyAsString());

                            System.out.println("Received response with status code" + response1.statusCode());
                            System.out.println("Received response body" + response1.bodyAsString());
                        } else {
                            System.out.println("Something went wrong " + ar.cause().getMessage());
                        }
                    });
             */

        });



        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();

            response
                                    .putHeader("content-type","text/html")
                                    .end("<h1>Hello!</h1> Type an url after localhost.");
                    });

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port",8080),
                        result -> {
                            if(result.succeeded()){
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }
}
