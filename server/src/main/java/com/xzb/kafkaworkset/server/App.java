package com.xzb.kafkaworkset.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Vertx vertx = Vertx.vertx();
    	HttpServer server = vertx.createHttpServer();

    	server.requestHandler(request -> {

    	  // This handler gets called for each request that arrives on the server
    	  HttpServerResponse response = request.response();
    	  response.putHeader("content-type", "text/plain");

    	  // Write to the response and end it
    	  response.end("Hello World!\n");
    	});
    	server.listen(8080);
    }
}
