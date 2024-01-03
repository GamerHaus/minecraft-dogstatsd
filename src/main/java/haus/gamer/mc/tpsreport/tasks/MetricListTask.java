package haus.gamer.mc.tpsreport.tasks;

import com.timgroup.statsd.StatsDClient;
import haus.gamer.mc.tpsreport.configuration.GlobalConfiguration;
import haus.gamer.mc.tpsreport.configuration.metric.MetricConfiguration;
import haus.gamer.mc.tpsreport.metrics.Metric;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MetricListTask extends BukkitServerTickRepeatingScheduledTask {
    List<MetricConfiguration> metricsToRun;
    StatsDClient statsdClient;

    public MetricListTask(GlobalConfiguration configuration,
                          JavaPlugin plugin,
                          int initialDelaySeconds,
                          int repeatDelaySeconds,
                          List<MetricConfiguration> metricsToRun,
                          StatsDClient statsdClient) {
        super(configuration, plugin, initialDelaySeconds, repeatDelaySeconds);
        this.metricsToRun = metricsToRun;
        // todo: would love some dependency injection but i dont feel like setting up guice rn
        this.statsdClient = statsdClient;
    }

    @Override
    public void run() {
        for (MetricConfiguration metricConfiguration : metricsToRun) {
            Metric m = metricConfiguration.buildConfiguredMetric(globalConfiguration.alwaysAddedTags);
            switch (metricConfiguration.mode) {
                case DATADOG:
                    m.emitStatsD(statsdClient);
                    break;
                case LOG:
                    m.emitLog();
                    break;
                case DISABLED:
                default:
                    break;
            }
        }

    }
}
