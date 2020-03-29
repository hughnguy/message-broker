package com.crunchshop.messagebroker.example.dpn;

import com.crunchshop.messagebroker.core.MessageConsumptionMode;
import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessMapper;
import com.crunchshop.messagebroker.core.message.eventmessage.EventMessageConsumer;
import com.crunchshop.messagebroker.event.AssetShared;

public class DPNBroadCastConsumer extends EventMessageConsumer {

    public DPNBroadCastConsumer() {
        super(new EventProcessMapper(AssetShared.class, new ClearCacheOnDatasourceUpdate()));
    }

    @Override
    public MessageConsumptionMode getMessageConsumptionMode() {
        return MessageConsumptionMode.BROADCAST_TO_ALL_CONSUMERS;
    }
}
