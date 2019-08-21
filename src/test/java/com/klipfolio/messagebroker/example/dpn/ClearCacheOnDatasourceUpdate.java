package com.klipfolio.messagebroker.example.dpn;

import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessor;
import com.klipfolio.messagebroker.event.AssetShared;

public class ClearCacheOnDatasourceUpdate implements EventProcessor<AssetShared> {

    @Override
    public void processEvent(AssetShared event) throws Exception {
        System.out.println("DPN clearing cache...");
    }
}
