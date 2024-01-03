package haus.gamer.mc.tpsreport;

import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import haus.gamer.mc.tpsreport.configuration.GlobalConfiguration;
import haus.gamer.mc.tpsreport.configuration.metric.MetricConfiguration;
import haus.gamer.mc.tpsreport.configuration.metric.PlayerPingsMetricConfiguration;
import haus.gamer.mc.tpsreport.tasks.BukkitServerTickRepeatingScheduledTask;
import haus.gamer.mc.tpsreport.tasks.MetricListTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TPSReportPlugin extends JavaPlugin {
    private BukkitServerTickRepeatingScheduledTask ticker;
    private List<BukkitServerTickRepeatingScheduledTask> metricTasks;
    private StatsDClient statsd;

    @Override
    public void onEnable() {
        // Set default config if not exists
        saveResource("config.yml", false);
        saveDefaultConfig();

        // parse configs
        GlobalConfiguration globalConfiguration = new GlobalConfiguration(getConfig());

        if (globalConfiguration.ddConfig.enabled) {
            statsd = new NonBlockingStatsDClientBuilder()
                    .hostname(globalConfiguration.ddConfig.host)
                    .port(globalConfiguration.ddConfig.port)
                    .build();
        } else {
            statsd = null;
        }
        // todo: could use reflection to grab all the metric config section implementations but :shruggies:
        List<MetricConfiguration> metrics = new ArrayList<>();
        metrics.add(new PlayerPingsMetricConfiguration(getConfig().getConfigurationSection(
                "tpsReport.metrics.playerPings")));
        Map<Integer, Map<Integer, List<MetricConfiguration>>> metricsByDelayAndPeriod = metrics.stream()
                                                                                               .collect(Collectors.groupingBy(
                                                                                                       m -> m.startDelaySeconds))
                                                                                               .entrySet().stream()
                                                                                               .collect(Collectors.toMap(
                                                                                                       Map.Entry::getKey,
                                                                                                       mByD -> mByD.getValue()
                                                                                                                   .stream()
                                                                                                                   .collect(
                                                                                                                           Collectors.groupingBy(
                                                                                                                                   m -> m.repeatPeriodSeconds))));

        metricsByDelayAndPeriod.forEach((delay, configsByPeriod) ->
                                                configsByPeriod.forEach((period, metricConfigs) -> metricTasks.add(new MetricListTask(
                                                        globalConfiguration,
                                                        this,
                                                        delay,
                                                        period,
                                                        metricConfigs,
                                                        statsd))));
        //todo: these aren't implemented yet (player count and tps metric types probably)
//        this.ticker = new RepeatingTask(this, 0, 20 * 10) {
//            @Override
//            public void run() {
//                //parse config for enabled metrics and modes
//
//                statsd.gauge("minecraft.players", Bukkit.getOnlinePlayers().size(), tags.toArray(new String[0]));
//
//                double[] tps = Bukkit.getServer().getTPS();
//
//                statsd.gauge("minecraft.tps.1", tps[0], tags.toArray(new String[0]));
//                statsd.gauge("minecraft.tps.5", tps[1], tags.toArray(new String[0]));
//                statsd.gauge("minecraft.tps.15", tps[2], tags.toArray(new String[0]));
//            }
//        };
    }

    @Override
    public void onDisable() {
        if (ticker != null) {
            ticker.cancelLoop();
        }
    }
}
