package com.klipfolio.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MetricCreated extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public MetricCreated(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.METRIC_CREATED.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
