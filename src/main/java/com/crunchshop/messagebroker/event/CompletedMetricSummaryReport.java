package com.crunchshop.messagebroker.event;

import com.crunchshop.messagebroker.core.exception.EventInvalidFieldException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompletedMetricSummaryReport implements Event {

    private static final int EVENT_VERSION = 1;

    private List<SummaryReport> summaryReports;
    private String userId;
    private Long currentDate;
    private Long previousDate;

    @Override
    public String getName() {
        return SupportedEvent.COMPLETED_METRIC_SUMMARY_REPORT.getName();
    }

    @Override
    public int getVersion() {
        return EVENT_VERSION;
    }

    @Override
    public Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();

        payload.put("summaryReports", summaryReports);
        payload.put("userId", userId);
        payload.put("currentDate", currentDate);
        payload.put("previousDate", previousDate);

        return payload;
    }

    @Override
    public void createFromPayload(Map<String, Object> payload) {
        summaryReports = (List<SummaryReport>) payload.get("summaryReports");
        userId = (String) payload.get("userId");
        currentDate = (Long) payload.get("currentDate");
        previousDate = (Long) payload.get("previousDate");
    }

    @Override
    public void validate() throws EventInvalidFieldException {
        if(userId == null) {
            throw new EventInvalidFieldException("User id is missing.");
        }
        if(summaryReports == null) {
            throw new EventInvalidFieldException("Missing list of summary reports.");
        }
        if(summaryReports.isEmpty()) {
            throw new EventInvalidFieldException("List of summary reports is empty.");
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class SummaryReport {
        String metricId;
        String metricName;
        String currencyCode;
        String numberFormat;
        Double currentValue;
        Double previousValue;
        String positiveDirection;
    }
}
