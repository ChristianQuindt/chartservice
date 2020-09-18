package de.itscope.chartclient.charts.strategies.mixpanelApiStrategies.segmentationApi;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import de.itscope.chartclient.charts.strategies.ChartStrategy;
import de.itscope.chartclient.charts.strategies.StackedChartStrategy;
import de.itscope.chartclient.model.mixpanelApi.segmentationApi.MPSegmentationApi;
import de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider.SegmentationApiProvider;

import java.net.URI;

public class SegmentationApiStackedBarStrategy extends StackedChartStrategy implements ChartStrategy<MPSegmentationApi> {
    private SegmentationApiProvider provider;
    private String yAxis;
    private int maxValues;
    private PlotOptionsSeries plotOptionsSeries;

    public SegmentationApiStackedBarStrategy(SegmentationApiProvider provider) {
        this.maxValues = 10;
        this.yAxis = "";
        this.provider = provider;
        setConfiguration("", "", "", "");
        setPlotoptions();
        setTooltip();
    }

    public SegmentationApiStackedBarStrategy(String title, String subtitle, String height, String width, int maxValues, String yAxis,
                                             SegmentationApiProvider provider) {
        this.maxValues = maxValues;
        this.yAxis = yAxis;
        this.provider = provider;
        setConfiguration(title, subtitle, height, width);
        setPlotoptions();
        setTooltip();
    }

    public SegmentationApiStackedBarStrategy(String title, String subtitle,
                                             SegmentationApiProvider provider) {
        this.maxValues = 10;
        this.yAxis = "";
        this.provider = provider;
        setConfiguration(title, subtitle, "", "");
        setPlotoptions();
        setTooltip();
    }

    public void setMaxValues(int maxValues) {
        this.maxValues = maxValues;
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
    protected void setConfiguration(String title, String subtitle, String height, String width) {
        super.setConfiguration(title, subtitle, height, width);
        Configuration conf = chart.getConfiguration();
        conf.getChart().setType(ChartType.BAR);
        conf.getyAxis().setTitle(yAxis);
        conf.getLegend().setEnabled(false);
    }

    @Override
    protected void setPlotoptions() {
        PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
        plotOptionsSeries.setStacking(Stacking.NORMAL);
        plotOptionsSeries.setDataLabels(new DataLabels(true));
        this.plotOptionsSeries = plotOptionsSeries;
        chart.getConfiguration().setPlotOptions(this.plotOptionsSeries);
    }

    private void addData(MPSegmentationApi data) {

        throw new java.lang.UnsupportedOperationException("This function still needs to be implemented.");
    }
}
