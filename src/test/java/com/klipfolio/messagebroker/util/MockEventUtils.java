package com.klipfolio.messagebroker.util;

import com.klipfolio.messagebroker.core.message.eventmessage.EventMessage;
import com.klipfolio.messagebroker.core.message.eventmessage.EventMessageSerializer;
import com.klipfolio.messagebroker.event.AssetShared;
import com.klipfolio.messagebroker.event.Event;

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
