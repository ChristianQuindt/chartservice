package de.itscope.chartclient.charts.strategies.mixpanelApiStrategies.funnelsApi;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.DataSeriesItem;
import de.itscope.chartclient.charts.strategies.ChartStrategy;
import de.itscope.chartclient.charts.strategies.FunnelFromColumnStrategy;
import de.itscope.chartclient.model.mixpanelApi.funnelsApi.MPFunnelsApi;
import de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider.FunnelsApiProvider;

import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FunnelsApiFunnelFromColumnStrategy extends FunnelFromColumnStrategy implements ChartStrategy<MPFunnelsApi> {
    private FunnelsApiProvider provider;

    public FunnelsApiFunnelFromColumnStrategy(String title, String subtitle, String height, String width, FunnelsApiProvider provider) {
        super(title, subtitle, height, width);
        this.provider = provider;
    }

    public FunnelsApiFunnelFromColumnStrategy(FunnelsApiProvider provider) {
        super("", "", "", "");
        this.provider = provider;
    }

    public FunnelsApiFunnelFromColumnStrategy(String title, String subtitle, FunnelsApiProvider provider) {
        super(title, subtitle, "", "");
        this.provider = provider;
    }

    @Override
    public <T> Chart buildChart(T data) {
        addDataBackground((MPFunnelsApi) data);
        addDataForeground((MPFunnelsApi) data);
        return chart;
    }

    @Override
    public URI getApiUri() {
        return provider.compose();
    }

    @Override
    public Class<MPFunnelsApi> getReturnType() {
        return FunnelsApiProvider.returnType;
    }

    protected void addDataBackground(MPFunnelsApi mpData) {
        List<Map.Entry<String, Integer>> collect = provider.prepareDataStructure(mpData).entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue((Comparator.comparingInt(o -> o))))).collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : collect) {
            //No clipping at the start and end of the areas's line with bars in the foreground
            backgroundSeries.add(new DataSeriesItem(entry.getKey(), entry.getValue() * 0.98));
            chart.getConfiguration().getxAxis().addCategory(entry.getKey());
        }
        chart.getConfiguration().addSeries(backgroundSeries);
    }

    protected void addDataForeground(MPFunnelsApi mpData) {
        List<Map.Entry<String, Integer>> collect = provider.prepareDataStructure(mpData).entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue((Comparator.comparingInt(o -> o))))).collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : collect) {
            foregroundSeries.add(new DataSeriesItem(entry.getKey(), entry.getValue()));
            chart.getConfiguration().getxAxis().addCategory(entry.getKey());
        }
        chart.getConfiguration().addSeries(foregroundSeries);
    }
}
