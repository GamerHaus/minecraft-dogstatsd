package haus.gamer.mc.tpsreport.configuration.metric;

import haus.gamer.mc.tpsreport.metrics.Metric;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public abstract class MetricConfiguration {
    public MetricMode mode;
    public int startDelaySeconds = 0;
    public int repeatPeriodSeconds = 10;
    public String metricName;

    public MetricConfiguration(ConfigurationSection configurationSection) {
        mode = MetricMode.valueOf(configurationSection.getString("mode"));
        startDelaySeconds = configurationSection.getInt("startDelaySeconds");
        repeatPeriodSeconds = configurationSection.getInt("repeatPeriodSeconds");
        metricName = configurationSection.getString("metricName");
    }

    public abstract Metric buildConfiguredMetric(List<String> alwaysAddedTags);
}
