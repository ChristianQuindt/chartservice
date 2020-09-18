package de.itscope.chartclient.charts.strategies;

import com.vaadin.addon.charts.Chart;
import de.itscope.chartclient.model.RestReturnType;;

import java.net.URI;
import java.util.List;

public interface ChartStrategy<T extends RestReturnType> {

	<T> Chart buildChart(T data);

	URI getApiUri();

	Class<T> getReturnType();

}
