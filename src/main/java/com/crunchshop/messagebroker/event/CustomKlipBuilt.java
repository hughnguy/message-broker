package com.crunchshop.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CustomKlipBuilt extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public CustomKlipBuilt(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.CUSTOM_KLIP_BUILT.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
