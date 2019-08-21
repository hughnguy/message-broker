package com.klipfolio.messagebroker.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DatasourceCreated extends UserEvent {

    private static final int EVENT_VERSION = 1;

    public DatasourceCreated(String userId) {
        super(userId);
    }

    @Override
    public String getName() {
        return SupportedEvent.DATASOURCE_CREATED.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }
}
