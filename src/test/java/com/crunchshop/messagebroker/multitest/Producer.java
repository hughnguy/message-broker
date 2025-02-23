package com.crunchshop.messagebroker.multitest;

import com.crunchshop.messagebroker.event.Test1Event;
import com.crunchshop.messagebroker.event.Test2Event;
import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.core.MessageBrokerManager;
import com.crunchshop.messagebroker.core.MessageBrokerManagerFactory;
import com.crunchshop.messagebroker.core.exception.EventPublishException;
import com.crunchshop.messagebroker.impl.aws.AWSMessageBrokerManager;

public class Producer {
	public static void main(String[] args) {
		MessageBrokerManager messageBrokerManager = new AWSMessageBrokerManager("", "", "ca-central-1");
		MessageBroker.init("mbartel-test", "mbtest-01", "mbartel-42-mbtest-01", new MessageBrokerManagerFactory(messageBrokerManager));


		try {
			for (int i = 0; i < 10000; i++) {
				if (i%1000 == 0) {
					MessageBroker.getInstance().publishEvent(new Test1Event());
				} else {
					MessageBroker.getInstance().publishEvent(new Test2Event());
				}
			}
		} catch (EventPublishException e) {
			e.printStackTrace();
		}
	}
}
