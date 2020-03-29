package com.crunchshop.messagebroker.example.dpn;

import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessor;
import com.crunchshop.messagebroker.event.AssetShared;

public class ClearCacheOnDatasourceUpdate implements EventProcessor<AssetShared> {

    @Override
    public void processEvent(AssetShared event) throws Exception {
        System.out.println("DPN clearing cache...");
    }
}
