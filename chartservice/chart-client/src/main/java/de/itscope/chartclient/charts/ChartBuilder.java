package de.itscope.chartclient.charts;

import com.vaadin.addon.charts.Chart;
import de.itscope.chartclient.charts.strategies.ChartStrategy;
import de.itscope.chartclient.model.RestReturnType;
import de.itscope.chartclient.provider.DataProvider;

import java.util.List;

public class ChartBuilder {
    private final DataProvider provider;

    public ChartBuilder(String mpSecret) {
        this.provider = new DataProvider(mpSecret);
    }

    public Chart build(ChartStrategy strategy) {
        Chart chart = strategy.buildChart(provider.provide(strategy.getApiUri(), strategy.getReturnType()));
        return chart;

    }
}
