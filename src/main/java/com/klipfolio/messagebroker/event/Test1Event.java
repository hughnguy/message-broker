package com.klipfolio.messagebroker.event;

import com.klipfolio.messagebroker.core.exception.EventInvalidFieldException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Test1Event implements Event {

	static public final AtomicInteger nextId = new AtomicInteger(0);

	public long delta;
	public int id;

	@Override
	public String getName() {
		return "test1";
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public Map<String, Object> getPayload() {

		Map<String, Object> payload = new HashMap<>();
		payload.put("timestamp", System.currentTimeMillis());
		payload.put("id", nextId.incrementAndGet());
		return payload;
	}

	@Override
	public void createFromPayload(Map<String, Object> payload) {
		long now = System.currentTimeMillis();
		long ts = (long) payload.get("timestamp");
		this.delta = now - ts;
		this.id = (int) payload.get("id");
	}

	@Override
	public void validate() throws EventInvalidFieldException {

	}
}
