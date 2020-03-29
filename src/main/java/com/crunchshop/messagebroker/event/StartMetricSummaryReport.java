package com.crunchshop.messagebroker.event;

import com.crunchshop.messagebroker.core.exception.EventInvalidFieldException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("PMD")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StartMetricSummaryReport implements Event {

    private static final int EVENT_VERSION = 1;

    private List<String> userIdsRequiringMetricSummaryReport;

    @Override
    public String getName() {
        return SupportedEvent.START_METRIC_SUMMARY_REPORT.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }

    @Override
    public Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();

        payload.put("userIdsRequiringMetricSummaryReport", userIdsRequiringMetricSummaryReport);

        return payload;
    }

    @Override
    public void createFromPayload(Map<String, Object> payload) {
        userIdsRequiringMetricSummaryReport = (List<String>) payload.get("userIdsRequiringMetricSummaryReport");
    }

    @Override
    public void validate() throws EventInvalidFieldException {
        if(userIdsRequiringMetricSummaryReport == null) {
            throw new EventInvalidFieldException("Missing list of user ids.");
        }
        if(userIdsRequiringMetricSummaryReport.isEmpty()) {
            throw new EventInvalidFieldException("List of user ids is empty.");
        }
    }
}
