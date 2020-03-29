package com.crunchshop.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MetricFilterChanged extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public MetricFilterChanged(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.METRIC_FILTER_CHANGED.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
