package com.xzb.kafkaworkset.client;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	SendUtil sendUtil = new SendUtil();
    	sendUtil.sendToCES();
    }
}
