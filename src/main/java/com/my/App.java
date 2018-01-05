package com.my;

import io.vertx.core.Vertx;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        Vertx vertx;

        //int port = 8081;
        vertx = Vertx.vertx();
        //ServerSocket serverSocket = new ServerSocket(0);
        //port = serverSocket.getLocalPort();
        //serverSocket.close();
        //DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port",port));
        vertx.deployVerticle(OKHttpVerticle.class.getName());
    }
}
