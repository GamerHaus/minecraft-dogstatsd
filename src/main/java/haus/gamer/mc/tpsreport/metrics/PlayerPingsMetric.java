package haus.gamer.mc.tpsreport.metrics;

import com.timgroup.statsd.StatsDClient;
import haus.gamer.mc.tpsreport.configuration.metric.PlayerPingsMetricConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerPingsMetric extends Metric {

    private final PlayerPingsMetricConfiguration config;

    public PlayerPingsMetric(PlayerPingsMetricConfiguration configSection, List<String> alwaysAddedTags) {
        super(configSection.metricName, alwaysAddedTags);
        config = configSection;
    }

    @Override
    public void emitStatsD(StatsDClient client) {
        List<PlayerPingGauge> metrics = getPlayerPingGauges();
        metrics.forEach(ppg -> client.gauge(ppg.metricName, ppg.pingValue, ppg.tags.toArray(new String[]{})));
    }

    @Override
    public void emitLog() {
        List<PlayerPingGauge> metrics = getPlayerPingGauges();
        metrics.forEach(ppg -> Bukkit.getLogger()
                                     .info(String.format("PlayerPingGauge - aspect: %s, pingValue: %d, tags: %s",
                                                         ppg.metricName,
                                                         ppg.pingValue,
                                                         ppg.tags)));
    }

    private List<PlayerPingGauge> getPlayerPingGauges() {
        List<PlayerPingGauge> result = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ArrayList<String> playerTags = new ArrayList<>();
            if (config.shouldShowPlayerNames) {
                playerTags.add("player_name:" + player.getName());
            } else {
                playerTags.add("player_id" + player.getUniqueId());
            }
            playerTags.addAll(alwaysAddedTags);
            result.add(new PlayerPingGauge(metricName, player.getPing(), playerTags));
        }
        return result;
    }

    private static class PlayerPingGauge {
        public String metricName;
        public int pingValue;
        public List<String> tags;

        public PlayerPingGauge(String metricName, int pingValue, List<String> tags) {
            this.metricName = metricName;
            this.pingValue = pingValue;
            this.tags = tags;
        }
    }
}
