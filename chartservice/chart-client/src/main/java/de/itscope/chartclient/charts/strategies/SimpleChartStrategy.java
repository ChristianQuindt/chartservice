package de.itscope.chartclient.charts.strategies;


import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Tooltip;
import org.springframework.util.StringUtils;

public abstract class SimpleChartStrategy {
    protected Chart chart = new Chart();
    private Tooltip toolTip;

    protected SimpleChartStrategy() {
    }

    protected void setConfiguration(String title, String subtitle, String height, String width, ChartType chartType) {
        Configuration conf = chart.getConfiguration();
        conf.getChart().setType(chartType);
        conf.setTitle(title);
        conf.setSubTitle(subtitle);
        if (!StringUtils.isEmpty(height)) {
            chart.setHeight(height);
        } else {
            chart.setHeight("100%");
        }
        if (!StringUtils.isEmpty(width)) {
            chart.setWidth(width);
        } else {
            chart.setWidth("100%");
        }
    }

    public Tooltip getToolTip() {
        return toolTip;
    }


    public Configuration getConfiguration() {
        return chart.getConfiguration();
    }

    protected abstract void setPlotoptions();

    protected void setTooltip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setUseHTML(true);
        tooltip.setHeaderFormat("{point.key}: ");
        tooltip.setPointFormat("");
        tooltip.setFooterFormat("{point.y:,.0f}");
        this.toolTip = tooltip;
        chart.getConfiguration().setTooltip(this.toolTip);
    }
}
