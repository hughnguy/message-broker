package com.crunchshop.messagebroker.core.message.eventmessage;

import com.crunchshop.messagebroker.event.StartMetricSummaryReport;
import com.crunchshop.messagebroker.mock.MockAssetSharedConsumer;
import com.crunchshop.messagebroker.mock.MockAssetSharedProcessor;
import com.crunchshop.messagebroker.core.MessageConsumer;
import com.crunchshop.messagebroker.core.Topic;
import com.crunchshop.messagebroker.core.exception.MessageDiscardException;
import com.crunchshop.messagebroker.core.exception.MessageRequeueException;
import com.crunchshop.messagebroker.core.exception.EventInvalidFieldException;
import com.crunchshop.messagebroker.event.AssetShared;
import com.crunchshop.messagebroker.mock.MockMessageBrokerManager;
import com.crunchshop.messagebroker.util.MockEventUtils;
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
