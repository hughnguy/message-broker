package com.crunchshop.messagebroker.multitest;

import com.crunchshop.messagebroker.MessageBroker;
import com.crunchshop.messagebroker.event.Test1Event;
import com.crunchshop.messagebroker.event.Test2Event;
import com.crunchshop.messagebroker.core.MessageBrokerManager;
import com.crunchshop.messagebroker.core.MessageBrokerManagerFactory;
import com.crunchshop.messagebroker.core.message.eventmessage.EventMessageConsumer;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessMapper;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessor;
import com.crunchshop.messagebroker.impl.aws.AWSMessageBrokerManager;

public class Consumer {
	public static void main(String[] args) {
		MessageBrokerManager messageBrokerManager = new AWSMessageBrokerManager("", "", "ca-central-1");
		MessageBroker.init("mbartel-test", "mbtest-01", "mbartel-42-mbtest-01", new MessageBrokerManagerFactory(messageBrokerManager));

		EventMessageConsumer consumer = new MessageConsumer();

		System.out.printf("Message consumer created.\n");

	}

	private static class MessageConsumer extends EventMessageConsumer {
		public MessageConsumer() {
			super(
					new EventProcessMapper(Test1Event.class, new Processor()),
					new EventProcessMapper(Test2Event.class, new Processor())
			);
			System.out.printf("Message consumer constructed.\n");
		}

		public int getMaxReceiveMessageCount() {
			return 10;
		}
	}

	private static class Processor implements EventProcessor<Test1Event> {

		private long total;
		private long max;
		private int count;
		private int longCount;

		public void processEvent(Test1Event event) throws Exception {
			this.count++;
			this.total += event.delta;
			if (event.delta > this.max) {
				this.max = event.delta;
			}
			if (event.delta > 1000) {
				this.longCount++;
			}
			long average = this.total / this.count;
			System.out.printf("Message %s with id %d delivered in %fs (average message time %fs, max %fs, number of messages delayed longer than 1s is %d).\n", event.getName(), event.id, event.delta/1000.0, average/1000.0, max/1000.0, longCount);
		}
	}
}
