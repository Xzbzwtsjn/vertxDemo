package com.cloud.dms;

import static com.cloud.dms.ApiUtils.acknowledgeMessages;
import static com.cloud.dms.ApiUtils.consumeMessages;
import static com.cloud.dms.ApiUtils.parseHandlerIds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MsgConsumer implements Runnable{
	
	private MsgAttri msgAttri;
	
	private String tag;

	
	public MsgConsumer(MsgAttri msg, String tag) {
		this.tag = tag;
		this.msgAttri = msg;
	}
	
	@Override
	public void run() {
		while (true)
		{
			ResponseMessage consumeMessagesResMsg = consumeMessages(msgAttri, tag);
			List<String> msgStrings = ApiUtils.decodeMsg(consumeMessagesResMsg);
			msgStrings.stream().forEach(s->
			{
				System.out.println("Thread--"+ Thread.currentThread().getName() 
						+ "--Message Body is: "+ s);
			});
			
			ArrayList<String> handlerIds = parseHandlerIds(consumeMessagesResMsg);
			if (handlerIds.size() > 0)
			{
				acknowledgeMessages(handlerIds, msgAttri);
			}
			try 
			{
				TimeUnit.SECONDS.sleep(1);
			} 
			catch (InterruptedException e) {}
		}
	}

}
