package com.klipfolio.messagebroker.example.webui;

import com.klipfolio.messagebroker.core.message.eventmessage.EventProcessor;
import com.klipfolio.messagebroker.event.AssetShared;

public class SendEmailOnDatasourceUpdate implements EventProcessor<AssetShared> {

    @Override
    public void processEvent(AssetShared event) throws Exception {
        System.out.println("WEBUI sending email notification...");
    }
}
