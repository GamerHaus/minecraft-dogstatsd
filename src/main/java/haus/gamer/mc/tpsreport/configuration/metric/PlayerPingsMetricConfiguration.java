package haus.gamer.mc.tpsreport.configuration.metric;

import haus.gamer.mc.tpsreport.metrics.Metric;
import haus.gamer.mc.tpsreport.metrics.PlayerPingsMetric;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class PlayerPingsMetricConfiguration extends MetricConfiguration {
    public boolean shouldShowPlayerNames;

    public PlayerPingsMetricConfiguration(ConfigurationSection configurationSection) {
        super(configurationSection);
        shouldShowPlayerNames = configurationSection.getBoolean("shouldShowPlayerNames");
    }

    @Override
    public Metric buildConfiguredMetric(List<String> alwaysAddedTags) {
        return new PlayerPingsMetric(this, alwaysAddedTags);
    }
}
