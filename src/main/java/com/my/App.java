package com.my;

import com.my.learn.MyFirstVerticle;
import com.my.learn.OKHttpVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.net.ServerSocket;

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
        //ServerSocket serverSocket = new ServerSocket(0);//这会随机选择一个端口
        //port = serverSocket.getLocalPort();
        //serverSocket.close();
        //DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port",port));
        vertx.deployVerticle(OKHttpVerticle.class.getName());
    }
}
