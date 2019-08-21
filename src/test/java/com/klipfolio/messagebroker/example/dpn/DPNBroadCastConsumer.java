package com.klipfolio.messagebroker.example.dpn;

import com.klipfolio.messagebroker.core.MessageConsumptionMode;
import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessMapper;
import com.klipfolio.messagebroker.core.message.eventmessage.EventMessageConsumer;
import com.klipfolio.messagebroker.event.AssetShared;

public class DPNBroadCastConsumer extends EventMessageConsumer {

    public DPNBroadCastConsumer() {
        super(new EventProcessMapper(AssetShared.class, new ClearCacheOnDatasourceUpdate()));
    }

    @Override
    public MessageConsumptionMode getMessageConsumptionMode() {
        return MessageConsumptionMode.BROADCAST_TO_ALL_CONSUMERS;
    }
}
