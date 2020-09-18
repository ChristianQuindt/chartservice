package de.itscope.chartclient.charts.strategies.mixpanelApiStrategies.segmentationApi;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import de.itscope.chartclient.charts.strategies.ChartStrategy;
import de.itscope.chartclient.charts.strategies.SimpleChartStrategy;
import de.itscope.chartclient.model.mixpanelApi.segmentationApi.MPSegmentationApi;
import de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider.SegmentationApiProvider;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SegmentationApiLineStrategy extends SimpleChartStrategy implements ChartStrategy<MPSegmentationApi> {
    private SegmentationApiProvider provider;
    private PlotOptionsLine plotOptionsLine;
    private int limit;

    public SegmentationApiLineStrategy(String title, String subtitle, String height, String width, int limit, SegmentationApiProvider provider) {
        this.provider = provider;
        this.limit = limit;
        setConfiguration(title, subtitle, height, width, ChartType.LINE);
        setPlotoptions();
        setTooltip();
    }

    public SegmentationApiLineStrategy(String title, String subtitle, SegmentationApiProvider provider) {
        this.provider = provider;
        this.limit = 5;
        setConfiguration(title, subtitle, "", "", ChartType.LINE);
        setPlotoptions();
        setTooltip();
    }

    public SegmentationApiLineStrategy(SegmentationApiProvider provider) {
        this.provider = provider;
        this.limit = 5;
        setConfiguration("", "", "", "", ChartType.LINE);
        setPlotoptions();
        setTooltip();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public PlotOptionsLine getPlotOptionsLine() {
        return plotOptionsLine;
    }

    @Override
    public <T> Chart buildChart(T data) {
        addData((MPSegmentationApi) data);
        return chart;
    }

    private void addData(MPSegmentationApi data) {
        Set<Map.Entry<String, Map<String, Integer>>> stringMapMap = provider.prepareDataStructure(data).entrySet();
        AtomicReference<ArrayList<String>> series = new AtomicReference<>(new ArrayList<>());
        List<String> dates = stringMapMap.stream().findFirst().get().getValue().keySet().stream().sorted().collect(Collectors.toList());
        series.get().addAll(dates);

        if (data.getData().getSeries() != null) {
            series.get().addAll(data.getData().getSeries());
        }
        chart.getConfiguration().getxAxis().setCategories(series.get().toArray(String[]::new));
        List<Map.Entry<String, Map<String, Integer>>> collect = provider.prepareDataStructure(data).entrySet().stream()
                .sorted(Collections.reverseOrder(
                        Comparator.comparingInt(o -> o.getValue().values().stream().reduce(Integer::sum).get()))
                )
                .limit(limit).collect(Collectors.toList());


        collect.stream().forEach(e -> {
            ListSeries ls = new ListSeries();
            ls.setName(e.getKey());
            Map<String, Integer> values = e.getValue();
            for (String date : series.get()) {
                ls.addData(values.get(date));
            }
            chart.getConfiguration().addSeries(ls);
        });
    }

    @Override
    protected void setConfiguration(String title, String subtitle, String height, String width, ChartType chartType) {
        super.setConfiguration(title, subtitle, height, width, chartType);
        chart.getConfiguration().getLegend().setEnabled(false);
    }


    @Override
    public URI getApiUri() {
        return provider.compose();
    }

    @Override
    public Class<MPSegmentationApi> getReturnType() {
        return SegmentationApiProvider.returnType;
    }

    @Override
    protected void setPlotoptions() {
        PlotOptionsLine plotOptionsLine = new PlotOptionsLine();
        plotOptionsLine.getDataLabels().setEnabled(false);
        plotOptionsLine.setMarker(new Marker(false));
        this.plotOptionsLine = plotOptionsLine;
        chart.getConfiguration().setPlotOptions(this.plotOptionsLine);
    }
}
