package haus.gamer.mc.tpsreport.metrics;

import com.timgroup.statsd.StatsDClient;
import org.bukkit.Bukkit;

import java.util.List;

public abstract class Metric {
    protected String metricName;
    protected List<String> alwaysAddedTags;

    public Metric(String metricName, List<String> alwaysAddedTags) {
        this.metricName = metricName;
        this.alwaysAddedTags = alwaysAddedTags;
    }

    public void emitStatsD(StatsDClient client) {
        client.count(metricName, 1L, alwaysAddedTags.toArray(new String[0]));
    }

    public void emitLog() {
        Bukkit.getLogger().info(this.toString());
    }

    @Override
    public String toString() {
        return "Metric{" +
                "aspect='" + metricName + '\'' +
                ", value='" + 1 + '\'' +
                ", tags=" + alwaysAddedTags +
                '}';
    }
}
