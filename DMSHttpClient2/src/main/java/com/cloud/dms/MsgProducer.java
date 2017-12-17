package com.cloud.dms;

import static com.cloud.dms.ApiUtils.constructTempMessages;
import static com.cloud.dms.ApiUtils.sendMessages;

import java.util.concurrent.TimeUnit;

public class MsgProducer implements Runnable{
	private MsgAttri msgAttri;

	public MsgProducer(MsgAttri msg) {
		msgAttri = msg;
	}
	
	@Override
	public void run() {
		while (true)
		{
			String messages = constructTempMessages();
			sendMessages(messages, msgAttri);
			try
			{
				TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
			}
		}
	}

}
