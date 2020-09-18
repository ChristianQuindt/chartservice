package de.itscope.chartclient.charts.strategies;


import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Tooltip;
import org.springframework.util.StringUtils;

public abstract class StackedChartStrategy {
    protected Chart chart = new Chart();
    private Tooltip toolTip;

    protected StackedChartStrategy() {
    }

    public Tooltip getToolTip() {
        return toolTip;
    }

    public Configuration getConfiguration() {
        return chart.getConfiguration();
    }

    protected void setConfiguration(String title, String subtitle, String height, String width) {
        Configuration conf = chart.getConfiguration();
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
