package com.klipfolio.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class KlipAddedToDashboard extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public KlipAddedToDashboard(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.KLIP_ADDED_TO_DASHBOARD.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
