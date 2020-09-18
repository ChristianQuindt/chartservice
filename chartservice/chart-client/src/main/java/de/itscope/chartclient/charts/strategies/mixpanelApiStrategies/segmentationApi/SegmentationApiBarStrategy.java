package de.itscope.chartclient.charts.strategies.mixpanelApiStrategies.segmentationApi;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import de.itscope.chartclient.charts.strategies.ChartStrategy;
import de.itscope.chartclient.charts.strategies.SimpleChartStrategy;
import de.itscope.chartclient.model.mixpanelApi.segmentationApi.MPSegmentationApi;
import de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider.SegmentationApiProvider;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class SegmentationApiBarStrategy extends SimpleChartStrategy implements ChartStrategy<MPSegmentationApi> {

    private SegmentationApiProvider provider;
    private int valueLimit;
    private PlotOptionsBar plotOptionsBar;

    public SegmentationApiBarStrategy(String title, String subtitle, String height, String width, int valueLimit, SegmentationApiProvider provider) {
        this.provider = provider;
        this.valueLimit = valueLimit;
        setConfiguration(title, subtitle, height, width, ChartType.BAR);
        setPlotoptions();
        setTooltip();
    }

    public SegmentationApiBarStrategy(SegmentationApiProvider provider) {
        this.provider = provider;
        this.valueLimit = 5;
        setConfiguration("", "", "", "", ChartType.BAR);
        setPlotoptions();
        setTooltip();
    }

    public SegmentationApiBarStrategy(String title, String subtitle, SegmentationApiProvider provider) {
        this.provider = provider;
        this.valueLimit = 5;
        setConfiguration(title, subtitle, "", "", ChartType.BAR);
        setPlotoptions();
        setTooltip();
    }

    public void setValueLimit(int valueLimit) {
        if (valueLimit > 0) {
            this.valueLimit = valueLimit;
        } else {
            this.valueLimit = 1;
        }
    }

    public PlotOptionsBar getPlotOptionsBar() {
        return plotOptionsBar;
    }

    @Override
    public <T> Chart buildChart(T data) {
        addData((MPSegmentationApi) data);
        return chart;
    }

    private void addData(MPSegmentationApi data) {
        ArrayList<String> series = new ArrayList<>();
        ListSeries listSeries = new ListSeries();


        List<Map.Entry<String, Optional<Integer>>> shownValues = provider.prepareDataStructure(data)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        s -> s.getValue().values().stream().reduce(Integer::sum)))
                .entrySet()
                .stream()
                .filter(a -> a.getValue().isPresent()).sorted(
                        Collections.reverseOrder(Map.Entry.comparingByValue((Comparator.comparingInt(Optional::get))))).limit(valueLimit - 1).collect(Collectors.toList());
        shownValues.forEach(e -> {
            e.getValue().ifPresent(listSeries::addData);
            series.add(e.getKey());
        });

        Map<String, Optional<Integer>> collect = provider.prepareDataStructure(data)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        s -> s.getValue().values().stream().reduce(Integer::sum)))
                .entrySet()
                .stream()
                .filter(a -> a.getValue().isPresent()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        shownValues.forEach(v -> collect.remove(v.getKey()));
        Optional<Integer> otherValues = collect.values().stream().map(Optional::get).reduce(Integer::sum);

        otherValues.ifPresent(listSeries::addData);

        series.add("other");

        chart.getConfiguration().addSeries(listSeries);
        chart.getConfiguration().getxAxis().setCategories(series.toArray(String[]::new));
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
        PlotOptionsBar plotOptionsBar = new PlotOptionsBar();
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(false);
        plotOptionsBar.setDataLabels(dataLabels);
        this.plotOptionsBar = plotOptionsBar;
        chart.getConfiguration().setPlotOptions(this.plotOptionsBar);
    }
}
