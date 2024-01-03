package haus.gamer.mc.tpsreport;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import java.util.ArrayList;

public final class TPSReport extends JavaPlugin {
    private RepeatingTask ticker;
    private StatsDClient statsd;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveResource("config.yml", false);
        saveDefaultConfig();
        ArrayList<String> tags = new ArrayList<String>(getConfig().getStringList("tpsreport.tags"));

        statsd = new NonBlockingStatsDClientBuilder()
                .hostname(getConfig().getString("tpsreport.dataDog.host"))
                .port(getConfig().getInt("tpsreport.dataDog.port"))
                .build();

        this.ticker = new RepeatingTask(this, 0, 20 * 10) {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    ArrayList<String> playerTags = new ArrayList<String>();
                    playerTags.add("player_name:" + player.getName());
                    playerTags.add("uuid:" + player.getUniqueId());
                    playerTags.addAll(tags);
                    statsd.gauge("minecraft.player.latency", player.getPing(), playerTags.toArray(new String[0]));
                    statsd.gauge("minecraft.player.health", player.getHealth(), playerTags.toArray(new String[0]));
                    statsd.gauge("minecraft.player.food", player.getFoodLevel(), playerTags.toArray(new String[0]));
                    statsd.gauge("minecraft.player.level", player.getLevel(), playerTags.toArray(new String[0]));
                }

                statsd.gauge("minecraft.players", Bukkit.getOnlinePlayers().size(), tags.toArray(new String[0]));

                double[] tps = Bukkit.getServer().getTPS();
                statsd.gauge("minecraft.tps.1", tps[0], tags.toArray(new String[0]));
                statsd.gauge("minecraft.tps.5", tps[1], tags.toArray(new String[0]));
                statsd.gauge("minecraft.tps.15", tps[2], tags.toArray(new String[0]));

                for(World world : Bukkit.getServer().getWorlds()) {
                    ArrayList<String> worldTags = new ArrayList<String>();
                    worldTags.add("world:" + world.getName());
                    worldTags.addAll(tags);
                    statsd.gauge("minecraft.world.loaded_chunks", world.getLoadedChunks().length, worldTags.toArray(new String[0]));
                    statsd.gauge("minecraft.world.players", world.getPlayerCount(), worldTags.toArray(new String[0]));
                    statsd.gauge("minecraft.world.entities", world.getEntityCount(), worldTags.toArray(new String[0]));
                    statsd.gauge("minecraft.world.living_entities", world.getLivingEntities().size(), worldTags.toArray(new String[0]));
                }
            }
        };
    }

    @Override
    public void onDisable() {
        if(ticker != null) {
            ticker.cancelLoop();
        }
    }
}
