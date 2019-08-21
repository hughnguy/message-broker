package com.klipfolio.messagebroker.core.message.eventmessage;

import com.klipfolio.messagebroker.core.MessageConsumer;
import com.klipfolio.messagebroker.core.Topic;
import com.klipfolio.messagebroker.core.exception.MessageDiscardException;
import com.klipfolio.messagebroker.core.exception.MessageRequeueException;
import com.klipfolio.messagebroker.core.exception.EventInvalidFieldException;
import com.klipfolio.messagebroker.event.AssetShared;
import com.klipfolio.messagebroker.event.StartMetricSummaryReport;
import com.klipfolio.messagebroker.mock.MockAssetSharedConsumer;
import com.klipfolio.messagebroker.mock.MockAssetSharedProcessor;
import com.klipfolio.messagebroker.mock.MockMessageBrokerManager;
import com.klipfolio.messagebroker.util.MockEventUtils;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;

public class EventMessageValidatorTest {

    @Test
    public void testValidateEvent() throws Exception {
        MockAssetSharedConsumer mockConsumer = new MockAssetSharedConsumer(new MockAssetSharedProcessor());
        EventMessageValidator eventMessageValidator = new EventMessageValidator(mockConsumer);
        AssetShared assetShared = MockEventUtils.createMockEvent();
        eventMessageValidator.validate(assetShared);
    }

    @Test(expected = MessageRequeueException.class)
    public void testValidateWillRequeueEventIfConsumerIsNotListeningToEvent(@Mocked final MockMessageBrokerManager manager) throws Exception {
        MockAssetSharedConsumer mockConsumer = new MockAssetSharedConsumer(new MockAssetSharedProcessor());
        EventMessageValidator eventMessageValidator = new EventMessageValidator(mockConsumer);

        StartMetricSummaryReport startMetricSummaryReport = new StartMetricSummaryReport(null) {
            @Override
            public void validate() throws EventInvalidFieldException {}
        };

        eventMessageValidator.validate(startMetricSummaryReport);

        new Verifications() {{
            manager.removeMessageConsumerFromTopic((MessageConsumer) any, (Topic) any); times = 1;
        }};
    }

    @Test(expected = MessageDiscardException.class)
    public void testValidateThrowsMessageCorruptExceptionIfEventHasInvalidateField() throws Exception {
        MockAssetSharedConsumer mockConsumer = new MockAssetSharedConsumer(new MockAssetSharedProcessor());
        EventMessageValidator eventMessageValidator = new EventMessageValidator(mockConsumer);
        AssetShared assetShared = new AssetShared(null, null, null, null, null, null, null, null); // null fields will fail validation

        eventMessageValidator.validate(assetShared);
    }
}
