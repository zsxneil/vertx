package com.my.learn;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by neil on 2017/9/21.
 */
@RunWith(VertxUnitRunner.class)
public class MyFirstVerticleTest {

    private Vertx vertx;

    int port = 8081;


    @Before
    public void setup(TestContext testContext) throws IOException {
        vertx = Vertx.vertx();
        ServerSocket serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();
        serverSocket.close();
        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port",port));
        vertx.deployVerticle(MyFirstVerticle.class.getName(),options,testContext.asyncAssertSuccess());
    }

    @After
    public void  teardown(TestContext testContext) {
        vertx.close(testContext.asyncAssertSuccess());
    }

    @Test
    public void  testMyApplication(TestContext testContext) {
        final Async async = testContext.async();
        vertx.createHttpClient().getNow(port,"localhost","/",
                response->{
                    response.handler(body ->{
                        testContext.assertTrue(body.toString().contains("hello"));
                        async.complete();
                    });
                });
    }

}
