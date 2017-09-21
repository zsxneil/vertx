package com.my.learn;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neil on 2017/9/21.
 */
public class MyFirstVerticle extends AbstractVerticle {

    private Map<Integer,Whisky> products = new HashMap<>();

    private void createSomeData() {
        Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay");
        products.put(bowmore.getId(), bowmore);
        Whisky talisker = new Whisky("Talisker 57° North", "Scotland, Island");
        products.put(talisker.getId(), talisker);
    }

    private void getAll(RoutingContext context) {
        context.response()
                .putHeader("content-type","application/json; charset=utf-8")
                .end(Json.encodePrettily(products.values()));
    }

    private void addOne(RoutingContext context) {
        final Whisky whisky = Json.decodeValue(context.getBodyAsString(),Whisky.class);
        products.put(whisky.getId(),whisky);
        context.response()
                .setStatusCode(201)
                .putHeader("content-type","application/json;charset=utf-8")
                .end(Json.encodePrettily(whisky));
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        this.createSomeData();

        //create a Router Object
        Router router = Router.router(vertx);

        // Bind "/" to our hello message - so we are still compatible.
        router.route("/").handler(routerContext -> {
            HttpServerResponse response = routerContext.response();
            response.putHeader("content-type","text/html")
                    .end("<h1>hello from my first vert.x 3 application</h1>");
        });

        router.get("/api/whiskies").handler(this::getAll);
        router.route("/api/whiskies*").handler(BodyHandler.create());
        router.post("/api/whiskies").handler(this::addOne);
        router.route("/assets/*").handler(StaticHandler.create("assets"));

        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port",8080),result->{
                    if (result.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(result.cause());
                    }
        });


    }
}
