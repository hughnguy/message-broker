package com.klipfolio.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MetricBoardEnterEditMode extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public MetricBoardEnterEditMode(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.METRIC_BOARD_ENTER_EDIT_MODE.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
