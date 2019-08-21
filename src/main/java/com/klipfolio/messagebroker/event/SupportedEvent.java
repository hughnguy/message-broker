package com.klipfolio.messagebroker.event;

import java.util.HashMap;
import java.util.Map;

public enum SupportedEvent {

    ASSET_SHARED("AssetShared", AssetShared.class),
    COMPLETED_METRIC_SUMMARY_REPORT("CompletedMetricSummaryReport", CompletedMetricSummaryReport.class),
    START_METRIC_SUMMARY_REPORT("StartMetricSummaryReport", StartMetricSummaryReport.class),

	BLANK_DASHBOARD_ADDED("BlankDashboardAdded", BlankDashboardAdded.class),
    DATASOURCE_CREATED("DatasourceCreated", DatasourceCreated.class),
    CUSTOM_KLIP_BUILT("CustomKlipBuilt", CustomKlipBuilt.class),
    KLIP_ADDED_TO_DASHBOARD("KlipAddedToDashboard", KlipAddedToDashboard.class),

    METRIC_ADDED_TO_METRIC_BOARD("MetricAddedToMetricBoard", MetricAddedToMetricBoard.class),
    METRIC_BOARD_METRIC_VIEW_CHANGED("MetricBoardMetricViewChanged", MetricBoardMetricViewChanged.class),
	METRIC_BOARD_ENTER_EDIT_MODE("MetricBoardEnterEditMode", MetricBoardEnterEditMode.class),

    METRIC_BOARD_SAVED("MetricBoardSaved", MetricBoardSaved.class),
    METRIC_CHART_TYPE_CHANGED("MetricChartTypeChanged", MetricChartTypeChanged.class),
    METRIC_CREATED("MetricCreated", MetricCreated.class),
    METRIC_FILTER_CHANGED("MetricFilterChanged", MetricFilterChanged.class),
	METRIC_DATE_RANGE_CHANGED("MetricDateRangeChanged", MetricDateRangeChanged.class),
    METRIC_SEGMENTATION_CHANGED("MetricSegmentationChanged", MetricSegmentationChanged.class),

    TEST1("test1", Test1Event.class),
    TEST2("test2", Test2Event.class);


    private String name;
    private Class<? extends Event> eventClass;

    private static Map<String, SupportedEvent> nameMap = new HashMap<>(SupportedEvent.values().length);

    static {
        for (SupportedEvent supportedEvent : SupportedEvent.values()) {
            nameMap.put(supportedEvent.getName(), supportedEvent);
        }
    }

    SupportedEvent(String name, Class<? extends Event> eventClass) {
        this.name = name;
        this.eventClass = eventClass;
    }

    public static SupportedEvent getByName(String name) {
        return nameMap.get(name);
    }

    public Event getNewEventInstance() throws IllegalAccessException, InstantiationException {
        return eventClass.newInstance();
    }

    public String getName() {
        return name;
    }
}
