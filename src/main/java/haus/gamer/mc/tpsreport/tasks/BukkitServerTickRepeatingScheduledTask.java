package haus.gamer.mc.tpsreport.tasks;

import haus.gamer.mc.tpsreport.configuration.GlobalConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitServerTickRepeatingScheduledTask implements Runnable {
    private final int taskId;
    protected GlobalConfiguration globalConfiguration;

    public BukkitServerTickRepeatingScheduledTask(GlobalConfiguration configuration, JavaPlugin plugin, int initialDelaySeconds, int repeatDelaySeconds) {
        // todo: this could come from actual server stats if we want to try to be closer to 10 seconds even with worse tps
        int SERVER_TICKS = 20;
        this.taskId = Bukkit.getScheduler()
                            .scheduleSyncRepeatingTask(plugin,
                                                       this,
                                                       (long) initialDelaySeconds * SERVER_TICKS,
                                                       (long) repeatDelaySeconds * SERVER_TICKS);
        this.globalConfiguration = configuration;
    }

    public void cancelLoop() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }
}
