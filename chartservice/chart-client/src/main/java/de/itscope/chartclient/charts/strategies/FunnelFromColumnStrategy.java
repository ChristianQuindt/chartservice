package de.itscope.chartclient.charts.strategies;


import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import org.springframework.util.StringUtils;

public abstract class FunnelFromColumnStrategy {
    protected Chart chart = new Chart();
    protected PlotOptionsArea plotOptionsArea;
    protected PlotOptionsColumn plotOptionsColumn;
    protected DataSeries backgroundSeries;
    protected DataSeries foregroundSeries;
    private Tooltip toolTip;

    protected FunnelFromColumnStrategy(String title, String subtitle, String height, String width) {
        backgroundSeries = new DataSeries();
        foregroundSeries = new DataSeries();
        plotOptionsArea = new PlotOptionsArea();
        plotOptionsColumn = new PlotOptionsColumn();
        setConfiguration(title, subtitle, height, width);
        setPlotoptionsBackground();
        setPlotoptionsForeground();
        setTooltip();
    }

    public Tooltip getToolTip() {
        return toolTip;
    }

    public PlotOptionsArea getPlotOptionsArea() {
        return plotOptionsArea;
    }

    public PlotOptionsColumn getPlotOptionsColumn() {
        return plotOptionsColumn;
    }

    protected void setConfiguration(String title, String subtitle, String height, String width) {
        Configuration conf = chart.getConfiguration();

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

        conf.setTitle(title);
        conf.setSubTitle(subtitle);
        conf.getLegend().setEnabled(false);

        AxisTitle axisTitle = new AxisTitle();
        axisTitle.setText("");
        conf.getyAxis().setTitle(axisTitle);
    }

    public Configuration getConfiguration() {
        return chart.getConfiguration();
    }

    protected void setPlotoptionsBackground() {
        plotOptionsArea.getDataLabels().setEnabled(false);
        Marker marker = new Marker(false);
        States states = new States();
        states.setHover(new Hover(false));
        states.setSelect(new Select(false));
        marker.setStates(states);
        plotOptionsArea.setMarker(marker);
        plotOptionsArea.setEnableMouseTracking(false);

        backgroundSeries.setPlotOptions(plotOptionsArea);
    }

    protected void setPlotoptionsForeground() {
        States states = new States();
        states.setHover(new Hover(false));
        states.setSelect(new Select(false));
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        dataLabels.setRotation(0);
        dataLabels.setAlign(HorizontalAlign.CENTER);

        plotOptionsColumn.setDataLabels(dataLabels);
        plotOptionsColumn.setPointWidth(100);
        plotOptionsColumn.setStates(states);

        foregroundSeries.setPlotOptions(plotOptionsColumn);
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
