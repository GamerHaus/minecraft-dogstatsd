package haus.gamer.mc.tpsreport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingTask implements Runnable {
    private final int taskId;

    public RepeatingTask(JavaPlugin plugin, int initialDelay, int repeatDelay) {
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, (long)initialDelay, (long)repeatDelay);
    }

    public void cancelLoop() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }
}
