package de.itscope.chartservice.metrics;

import net.monofraps.influxmetrics.InfluxSeriesRegistry;
import net.monofraps.influxmetrics.internal.InfluxdbHttpReporter;
import net.monofraps.influxmetrics.internal.JmxReporter;
import net.monofraps.influxmetrics.jvm.BufferPoolMetrics;
import net.monofraps.influxmetrics.jvm.GarbageCollectorMetrics;
import net.monofraps.influxmetrics.jvm.MemoryUsageMetrics;
import net.monofraps.influxmetrics.jvm.ThreadStateMetrics;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.influxdb.InfluxDBIOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty("itscope.metrics.enabled")
@ConfigurationProperties("itscope.metrics")
public class MetricsSenderService extends MetricsReporterConfig {
    private Log logger = LogFactory.getLog(MetricsSenderService.class);
    private InfluxdbHttpReporter influxdbHttpReporter;
    private InfluxSeriesRegistry jvmMetricsRegistry = new InfluxSeriesRegistry();
    private InfluxSeriesRegistry requestServiceMetricsRegistry = new InfluxSeriesRegistry();
    private boolean connectedOnStartup;

    private static void registerJvmMetrics(InfluxSeriesRegistry registry) {
        new BufferPoolMetrics().registerSeries(registry);
        new GarbageCollectorMetrics().registerSeries(registry);
        new MemoryUsageMetrics().registerSeries(registry);
        new ThreadStateMetrics().registerSeries(registry);
    }

    @PreDestroy
    public void disable() {
        if (influxdbHttpReporter != null) {
            influxdbHttpReporter.stop();
        }
    }

    @PostConstruct
    public void enable() {
        try {
            final String hostname = java.net.InetAddress.getLocalHost().getHostName();
            registerJvmMetrics(jvmMetricsRegistry);

            final InfluxdbHttpReporter.Builder builder = InfluxdbHttpReporter.builder()
                    .tag("host", hostname)
                    .withHttpConnection(getHost(), String.valueOf(getPort()))
                    .withUser(getUser(), getPass())
                    .reportOwnStatistics(jvmMetricsRegistry);

            new JmxReporter(ManagementFactory.getPlatformMBeanServer(), jvmMetricsRegistry, "de.itscope.chartservice").start();
            new JmxReporter(ManagementFactory.getPlatformMBeanServer(), requestServiceMetricsRegistry, "de.itscope.chartservice").start();
            builder.withAdditionalRegistry(requestServiceMetricsRegistry, "chartservice");
            builder.withAdditionalRegistry(jvmMetricsRegistry, "chartservice_jvm");

            influxdbHttpReporter = builder.build();
            try {
                influxdbHttpReporter.start(getReportInterval(), TimeUnit.SECONDS);
                connectedOnStartup = true;
            } catch (InfluxDBIOException e) {
                logger.warn("Failed to connect to InfluxDB. Retrying in background...");
                new Thread(new InfluxHttpReporterStarter(influxdbHttpReporter, logger), "MetricSenderServiceStarter").start();
            }

        } catch (Exception e) {
            logger.error("Failed to enable metrics reporter", e);
        }
    }

    public InfluxSeriesRegistry getRequestServiceMetricsRegistry() {
        return requestServiceMetricsRegistry;
    }

    public boolean isConnected() {
        return connectedOnStartup && influxdbHttpReporter.isConnected();
    }

    private class InfluxHttpReporterStarter implements Runnable {

        private Log logger;
        private InfluxdbHttpReporter influxdbHttpReporter;

        public InfluxHttpReporterStarter(InfluxdbHttpReporter influxdbHttpReporter, Log logger) {
            this.influxdbHttpReporter = influxdbHttpReporter;
            this.logger = logger;
        }

        public void run() {
            while (true) {
                try {
                    influxdbHttpReporter.start(getReportInterval(), TimeUnit.SECONDS);
                    logger.info("metrics sender service is started");
                    connectedOnStartup = true;
                    break;
                } catch (InfluxDBIOException e) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException exeption) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

            }
        }
    }
}