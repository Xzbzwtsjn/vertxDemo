package com.xzb.kafkaworkset.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;

public class SendUtil {
	public void sendToCES() 
	{
		List<String> list1 = new ArrayList<>();
		List<String> list2 = new ArrayList<>();
		for(int i = 0;i < 10;i++)
		{
			list1.add("list1");
			list2.add("list2");
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("a", list1);
		map.put("b", list2);
		HttpClient client = Vertx.vertx().createHttpClient();
		List<Future> futures = new ArrayList<>();
		getToken().setHandler(res->
		{
			Vertx.vertx().executeBlocking(future->
			{
				map.forEach((k, v)->
				{
					v.forEach(u->
					{
						futures.add(sendToCes(u, client));
						System.out.println("future size is " + futures.size());
					});
				});
				CompositeFuture composite = CompositeFuture.join(futures).setHandler(r->
				{
					client.close();
					System.out.println("close http client!");
				});
				future.complete(composite);
			}, true, (ar)->
			{
				//((CompositeFuture)ar.result()).complete();
			});
		});
	}
	
	public Future<String> getToken()
	{
		Future<String> future = Future.future();
		getTokenFromIAM().setHandler(r->
		{
			future.complete(r.result());
		});
		return future;
	}
	
	private Future<String>getTokenFromIAM()
	{
		Future<String> future = Future.future();
		future.complete("Token");
		return future;
	}
	
	private Future<Void> sendToCes(String pars, HttpClient httpClient)
	{
		Future<Void> future = Future.future();
		send(future, httpClient);
		return future;
	}
	private void send(Future future, HttpClient httpClient)
	{
		httpSend(httpClient).setHandler(r->
		{
			future.complete();
		});
	}
	
	public Future<Integer> httpSend(HttpClient httpClient)
	{
		System.out.println("httpSend is ok");
		Future<Integer> future = Future.future();
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.setHost("127.0.0.1");
		requestOptions.setPort(8080);
		requestOptions.setSsl(false);
		httpClient.post(requestOptions, response->
		{
			
			try {
				TimeUnit.SECONDS.sleep(1);
				System.out.println("+" + response.statusMessage());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			future.complete(response.statusCode());
		}).end();
		return future;
	}
}
