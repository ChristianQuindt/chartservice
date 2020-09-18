package de.itscope.chartclient.charts.strategies;


import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import org.springframework.util.StringUtils;

public abstract class FunnelStrategy {

    protected Chart chart = new Chart();
    private PlotOptionsFunnel plotOptions;
    private Tooltip toolTip;

    protected FunnelStrategy(String title, String subtitle, String height, String width) {
        setConfiguration(title, subtitle, height, width);
        setTooltip();
        setPlotoptions();
    }

    public PlotOptionsFunnel getPlotOptions() {
        return plotOptions;
    }

    protected void setConfiguration(String title, String subtitle, String height, String width) {
        Configuration conf = chart.getConfiguration();
        conf.getChart().setType(ChartType.FUNNEL);
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
        conf.getLegend().setEnabled(false);
        conf.getChart().setSpacingRight(120);
    }

    protected void setPlotoptions() {
        PlotOptionsFunnel options = new PlotOptionsFunnel();
        options.setReversed(false);
        options.setNeckWidth("30%");
        options.setNeckHeight("30%");
        options.setWidth("70%");

        DataLabelsFunnel dataLabels = new DataLabelsFunnel();
        dataLabels.setFormat("{point.name} ({point.y:,.0f})");
        dataLabels.setSoftConnector(false);
        dataLabels.setInside(true);
        options.setDataLabels(dataLabels);

        this.plotOptions = options;
        chart.getConfiguration().setPlotOptions(this.plotOptions);
    }

    public Tooltip getToolTip() {
        return toolTip;
    }

    public Configuration getConfiguration() {
        return chart.getConfiguration();
    }

    private void setTooltip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        tooltip.setUseHTML(true);
        tooltip.setHeaderFormat("{point.key}: ");
        tooltip.setPointFormat("");
        tooltip.setFooterFormat("{point.y:,.0f}"); //{series.name}:
        this.toolTip = tooltip;
        chart.getConfiguration().setTooltip(this.toolTip);
    }
}
