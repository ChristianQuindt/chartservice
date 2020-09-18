package de.itscope.chartservice.metrics;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "itscope.metrics")
public class MetricsReporterConfig {

    /**
     * The host to which to send metrics to
     */
    private String host = "127.0.0.1";

    /**
     * The port at which to connect to the metrics receiving host
     */
    private int port = 8086;
    /**
     * The metrics report interval in seconds
     */
    private int reportInterval = 2;
    /**
     * The user to use when reporting metrics
     */
    private String user;
    /**
     * The report user's password
     */
    private String pass;

    public String getHost() {
        return host;
    }

    public String getPass() {
        return pass;
    }

    public int getPort() {
        return port;
    }

    public int getReportInterval() {
        return reportInterval;
    }

    public String getUser() {
        return user;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setReportInterval(int reportInterval) {
        this.reportInterval = reportInterval;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
