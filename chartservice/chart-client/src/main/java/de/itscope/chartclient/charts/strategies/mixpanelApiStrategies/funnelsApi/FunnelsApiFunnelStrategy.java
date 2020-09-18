package de.itscope.chartclient.charts.strategies.mixpanelApiStrategies.funnelsApi;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import de.itscope.chartclient.charts.strategies.ChartStrategy;
import de.itscope.chartclient.charts.strategies.FunnelStrategy;
import de.itscope.chartclient.model.mixpanelApi.funnelsApi.MPFunnelsApi;
import de.itscope.chartclient.provider.urlUtil.apiUriComposer.mixpanelApiProvider.FunnelsApiProvider;

import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FunnelsApiFunnelStrategy extends FunnelStrategy implements ChartStrategy<MPFunnelsApi> {

    private FunnelsApiProvider provider;

    public FunnelsApiFunnelStrategy(FunnelsApiProvider provider) {
        super("", "", "", "");
        setPlotoptions();
        this.provider = provider;
    }

    public FunnelsApiFunnelStrategy(String title, String subtitle, String height, String width, FunnelsApiProvider provider) {
        super(title, subtitle, height, width);
        this.provider = provider;
        setPlotoptions();
    }

    public FunnelsApiFunnelStrategy(String title, String subtitle, FunnelsApiProvider provider) {
        super(title, subtitle, "", "");
        setPlotoptions();
        this.provider = provider;
    }

    @Override
    public <T> Chart buildChart(T data) {
        addData((MPFunnelsApi) data);
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

    private void addData(MPFunnelsApi data) {
        List<Map.Entry<String, Integer>> collect = provider.prepareDataStructure(data).entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue((Comparator.comparingInt(o -> o))))).collect(Collectors.toList());
        ;

        DataSeries series = new DataSeries();
        for (Map.Entry<String, Integer> entry : collect) {
            series.add(new DataSeriesItem(entry.getKey(), entry.getValue()));
            chart.getConfiguration().getxAxis().addCategory(entry.getKey());
        }
        chart.getConfiguration().addSeries(series);
    }

}
