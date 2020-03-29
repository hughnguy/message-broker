package com.crunchshop.messagebroker.util;

import com.crunchshop.messagebroker.event.AssetShared;
import com.crunchshop.messagebroker.event.Event;
import com.crunchshop.messagebroker.core.message.eventmessage.EventMessage;
import com.crunchshop.messagebroker.core.message.eventmessage.EventMessageSerializer;

public class MockEventUtils {

    private MockEventUtils() {}

    public static EventMessage createMockEventMessageForConsumer(Event event) throws Exception {
        EventMessageSerializer eventMessageSerializer = new EventMessageSerializer();
        String eventPayload = eventMessageSerializer.serializeEventIntoPayload(event);
        String eventName = event.getName();
        int eventVersion = event.getVersion();

        return new EventMessage(
                "sourceService",
                "sourceEnvironment",
                "environmentSpecificEventName",
                eventName,
                eventPayload,
                eventVersion
        );
    }

    public static AssetShared createMockEvent() {
        return new AssetShared(
                "assetId",
                "assetName",
                "assetTypeDisplayName",
                "rawAssetType",
                "shareTargetType",
                "shareTargetId",
                "shareRightType",
                "sharerId"
        );
    }
}
