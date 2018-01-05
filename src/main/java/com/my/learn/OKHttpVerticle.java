package com.my.learn;

import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OKHttpVerticle extends AbstractVerticle {


    @Override
    public void start(Future<Void> startFuture) throws Exception {
        //create a Router Object
        Router router = Router.router(vertx);
        String tmpdir = System.getProperty("java.io.tmpdir");
        System.out.println(tmpdir);
        router.route().handler(BodyHandler.create().setUploadsDirectory(tmpdir));
        router.post("/okhttp/form").handler(this::postForm);
        router.post("/okhttp/json").handler(this::postJson);
        router.post("/okhttp/fileUpload").handler(this::uploadFile);
        router.get("/okhttp/download").handler(this::download);

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




    private void postForm(RoutingContext context) {
        //String username = context.request().getParam("username");
        String username = context.request().getFormAttribute("username");
        System.out.println(username);
        context.response().putHeader("content-type","application/json;charset=utf-8")
                .end(Json.encodePrettily(new JsonObject().put("username", "from vertx")));
    }
    private void postJson(RoutingContext context) {

        JsonObject json = context.getBodyAsJson();
        if (json != null) {
            System.out.println(json.toString());
            System.out.println(json.getString("username"));
        }

        context.response().putHeader("content-type","application/json;charset=utf-8")
                .end(Json.encodePrettily(new JsonObject().put("username", "from vertx")));
    }

    private void uploadFile(RoutingContext context) {
        context.response()
                .putHeader("content-type", "text/plain;charset=utf-8")
                .setChunked(true);
        try {
            for (FileUpload fileUpload : context.fileUploads()) {
                context.response()
                        .write("filename:" + fileUpload.fileName())
                        .write("\n")
                        .write("filesize:" + fileUpload.size())
                        .write("\n")
                        .write(fileUpload.uploadedFileName());

                Files.copy(Paths.get(fileUpload.uploadedFileName()),Paths.get("F:\\" + fileUpload.fileName()));
            }
            context.response().end();
        } catch (Exception e) {
            context.response().setStatusCode(500).end();
        }

    }

    private void download(RoutingContext context) {
        try {
            context.response()
                    .sendFile("F:/head_img.jpg");
                    //.end();
        } catch (Exception e) {
            e.printStackTrace();
            context.response().setStatusCode(500).end();
        }
    }
}
