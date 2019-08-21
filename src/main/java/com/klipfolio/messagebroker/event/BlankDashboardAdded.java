package com.klipfolio.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BlankDashboardAdded extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public BlankDashboardAdded(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.BLANK_DASHBOARD_ADDED.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
