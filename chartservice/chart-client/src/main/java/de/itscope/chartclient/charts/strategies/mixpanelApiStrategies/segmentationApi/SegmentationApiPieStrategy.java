package de.itscope.chartclient.charts.strategies.mixpanelApiStrategies.segmentationApi;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import de.itscope.chartclient.charts.strategies.ChartStrategy;
import de.itscope.chartclient.charts.strategies.SimpleChartStrategy;
import de.itscope.chartclient.model.mixpanelApi.segmentationApi.MPSegmentationApi;
import de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider.SegmentationApiProvider;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class SegmentationApiPieStrategy extends SimpleChartStrategy implements ChartStrategy<MPSegmentationApi> {

    private SegmentationApiProvider provider;

    private String innerSize;

    private int valueLimit;

    private PlotOptionsPie plotOptionsPie;

    public SegmentationApiPieStrategy(SegmentationApiProvider provider) {
        this.provider = provider;
        this.valueLimit = 10;
        this.innerSize = "65%";
        setConfiguration("", "", "", "", ChartType.PIE);
        setPlotoptions();
        setTooltip();
    }

    public SegmentationApiPieStrategy(String title, String subtitle, String height, String width, String innerSizePercent, int valueLimit, SegmentationApiProvider provider) {
        this.innerSize = innerSizePercent;
        this.provider = provider;
        this.valueLimit = valueLimit;
        setConfiguration(title, subtitle, height, width, ChartType.PIE);
        setPlotoptions();
        setTooltip();
    }

    public SegmentationApiPieStrategy(String title, String subtitle, SegmentationApiProvider provider) {
        this.provider = provider;
        this.valueLimit = 10;
        this.innerSize = "65%";
        setConfiguration(title, subtitle, "", "", ChartType.PIE);
        setPlotoptions();
        setTooltip();
    }

    public PlotOptionsPie getPlotOptionsPie() {
        return plotOptionsPie;
    }

    public void setValueLimit(int valueLimit) {
        this.valueLimit = valueLimit;
    }

    @Override
    public <T> Chart buildChart(T data) {
        addData((MPSegmentationApi) data);
        return chart;
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
    protected void setConfiguration(String title, String subtitle, String height, String width, ChartType chartType) {
        super.setConfiguration(title, subtitle, height, width, chartType);
        Legend legend = chart.getConfiguration().getLegend();
        legend.setEnabled(true);
        legend.setAlign(HorizontalAlign.RIGHT);
    }

    @Override
    protected void setPlotoptions() {
        PlotOptionsPie options = new PlotOptionsPie();
        options.setInnerSize(innerSize);
        options.setShowInLegend(true);
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(false);
        options.setDataLabels(dataLabels);
        this.plotOptionsPie = options;
        chart.getConfiguration().setPlotOptions(this.plotOptionsPie);
    }

    private void addData(MPSegmentationApi data) {
        DataSeries dataSeries = new DataSeries();
        int[] total = {0};
        List<Map.Entry<String, Optional<Integer>>> shownValues = provider.prepareDataStructure(data).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, s -> s.getValue().values().stream().reduce(Integer::sum)))
                .entrySet().stream().filter(d -> d.getValue().isPresent())
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue(Comparator.comparingInt(Optional::get))))
                .limit(valueLimit - 1).collect(Collectors.toList());

        shownValues.forEach(s -> {
            dataSeries.add(new DataSeriesItem(s.getKey(), s.getValue().get()));
            total[0] += s.getValue().get();
        });

        Map<String, Optional<Integer>> collect = provider.prepareDataStructure(data).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, s -> s.getValue().values().stream().reduce(Integer::sum)))
                .entrySet().stream().filter(d -> d.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        shownValues.forEach(v -> collect.remove(v.getKey()));
        Optional<Integer> otherValues = collect.values().stream().map(Optional::get).reduce(Integer::sum);
        otherValues.ifPresent(d -> {
            dataSeries.add(new DataSeriesItem("other", d));
            total[0] += d;
        });

        chart.getConfiguration().getSubTitle().setText(String.valueOf(total[0]));
        chart.getConfiguration().addSeries(dataSeries);
    }
}
