package haus.gamer.mc.tpsreport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TPSReport extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveResource("config.yml", false);
        saveDefaultConfig();

        //todo: periodically emit tps and player count metric gauge to ddog
        //todo: every 15 minutes/every time someone connects/disconnects giving a list of all players in the console would be amazing actually, lol

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
