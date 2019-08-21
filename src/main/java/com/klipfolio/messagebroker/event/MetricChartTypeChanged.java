package com.klipfolio.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MetricChartTypeChanged extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public MetricChartTypeChanged(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.METRIC_CHART_TYPE_CHANGED.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
