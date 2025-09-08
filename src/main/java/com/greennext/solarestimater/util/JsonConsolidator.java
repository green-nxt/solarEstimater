package com.greennext.solarestimater.util;

import com.greennext.solarestimater.model.MetricTitle;
import com.greennext.solarestimater.model.MetricValue;
import com.greennext.solarestimater.model.response.DailyGenerationResponseBody;

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
}
