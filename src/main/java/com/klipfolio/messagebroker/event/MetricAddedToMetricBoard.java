package com.klipfolio.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MetricAddedToMetricBoard extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public MetricAddedToMetricBoard(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.METRIC_ADDED_TO_METRIC_BOARD.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
