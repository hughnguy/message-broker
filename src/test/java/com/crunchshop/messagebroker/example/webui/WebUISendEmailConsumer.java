package com.crunchshop.messagebroker.example.webui;

import com.crunchshop.messagebroker.core.MessageConsumptionMode;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessMapper;
import com.crunchshop.messagebroker.core.message.eventmessage.EventMessageConsumer;
import com.crunchshop.messagebroker.event.AssetShared;

public class WebUISendEmailConsumer extends EventMessageConsumer {

    public WebUISendEmailConsumer() {
        super(new EventProcessMapper(AssetShared.class, new SendEmailOnDatasourceUpdate()));
    }

    @Override
    public MessageConsumptionMode getMessageConsumptionMode() {
        return MessageConsumptionMode.DELIVER_ONCE_PER_SERVICE;
    }
}
