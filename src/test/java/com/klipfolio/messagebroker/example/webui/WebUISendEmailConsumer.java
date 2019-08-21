package com.klipfolio.messagebroker.example.webui;

import com.klipfolio.messagebroker.core.MessageConsumptionMode;
import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessMapper;
import com.klipfolio.messagebroker.core.message.eventmessage.EventMessageConsumer;
import com.klipfolio.messagebroker.event.AssetShared;

public class WebUISendEmailConsumer extends EventMessageConsumer {

    public WebUISendEmailConsumer() {
        super(new EventProcessMapper(AssetShared.class, new SendEmailOnDatasourceUpdate()));
    }

    @Override
    public MessageConsumptionMode getMessageConsumptionMode() {
        return MessageConsumptionMode.DELIVER_ONCE_PER_SERVICE;
    }
}
