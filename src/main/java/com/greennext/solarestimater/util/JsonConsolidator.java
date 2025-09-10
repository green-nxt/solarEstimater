package com.greennext.solarestimater.util;

import com.greennext.solarestimater.model.MetricTitle;
import com.greennext.solarestimater.model.MetricValue;
import com.greennext.solarestimater.model.response.DailyGenerationResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JsonConsolidator {

    public static List<Map<String, String>> consolidatedTitleValueList(DailyGenerationResponseBody root) {
        List<MetricTitle> metaList = root.getGenerationResponseData().getMetricTitles();
        List<MetricValue> rows = root.getGenerationResponseData().getMetricValues();
        List<Map<String, String>> resultList = new ArrayList<>();
        for (MetricValue row : rows) {
            Map<String, String> item = new LinkedHashMap<>();
            for (int i = 0; i < metaList.size(); i++) {
                MetricTitle tm = metaList.get(i);
                String value = row.getMetricField().get(i);
                String valueWithUnit = (tm.getUnit() == null || tm.getUnit().isEmpty()) ? value : value + " " + tm.getUnit();
                item.put(tm.getUnit(), valueWithUnit);
            }
            resultList.add(item);
        }
        return resultList;
    }

    // Sum all numeric values, per title
    public static Map<String, Double> sumTitleNumeric(DailyGenerationResponseBody root) {
        List<MetricTitle> metaList = root.getGenerationResponseData().getMetricTitles();
        List<MetricValue> rows = root.getGenerationResponseData().getMetricValues();
        Map<String, Double> sumMap = new HashMap<>();
        for (int i = 0; i < metaList.size(); i++) {
            MetricTitle tm = metaList.get(i);
            String key = tm.getTitle() + ((tm.getUnit() == null || tm.getUnit().isEmpty()) ? "" : " (" + tm.getUnit() + ")");
            double sum = 0.0;
            boolean foundNumeric = false;
            for (MetricValue row : rows) {
                String value = row.getMetricField().get(i);
                try {
                    double num = Double.parseDouble(value);
                    sum += num;
                    foundNumeric = true;
                } catch (NumberFormatException ignore) {}
            }
            if (foundNumeric) {
                sumMap.put(key, sum);
            }
        }
        return sumMap;
    }

    // Assumes timestamp format is "yyyy-MM-dd HH:mm:ss"
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Find the row ("field" list) with the most recent timestamp
    public static MetricValue getLastRecordByTime(DailyGenerationResponseBody root) {
        List<MetricTitle> metaList = root.getGenerationResponseData().getMetricTitles();
        List<MetricValue> rows = root.getGenerationResponseData().getMetricValues();
        int timestampIndex = -1;
        // Find the index for "Timestamp"
        for (int i = 0; i < metaList.size(); i++) {
            if ("Timestamp".equalsIgnoreCase(metaList.get(i).getTitle())) {
                timestampIndex = i;
                break;
            }
        }
        if (timestampIndex == -1) throw new IllegalArgumentException("No Timestamp field found");

        MetricValue latestRow = null;
        LocalDateTime latestTime = null;
        for (MetricValue row : rows) {
            String tsValue = row.getMetricField().get(timestampIndex);
            LocalDateTime ts = LocalDateTime.parse(tsValue, formatter);
            if (latestTime == null || ts.isAfter(latestTime)) {
                latestTime = ts;
                latestRow = row;
            }
        }
        return latestRow;
    }
}
