package com.crunchshop.messagebroker.example.webui;

import com.crunchshop.messagebroker.core.message.eventmessage.EventProcessor;
import com.crunchshop.messagebroker.event.AssetShared;

public class SendEmailOnDatasourceUpdate implements EventProcessor<AssetShared> {

    @Override
    public void processEvent(AssetShared event) throws Exception {
        System.out.println("WEBUI sending email notification...");
    }
}
