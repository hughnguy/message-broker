package com.crunchshop.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MetricBoardMetricViewChanged extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public MetricBoardMetricViewChanged(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.METRIC_BOARD_METRIC_VIEW_CHANGED.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
