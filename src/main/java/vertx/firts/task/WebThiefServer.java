package vertx.firts.task;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
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

        //Create a router object
        Router router = Router.router(vertx);

        Route route = router.route(HttpMethod.GET,"/:siteUrl");

        route.handler(routingContext -> {
            String siteUrl = routingContext.request().getParam("siteUrl");

            HttpServerResponse response = routingContext.response();

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


        });



        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            client
                    .get(80, "lubimyczytac.pl","")
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
