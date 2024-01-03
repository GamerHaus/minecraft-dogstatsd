package haus.gamer.mc.tpsreport.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class DataDogConfiguration extends ConfigParser {
    public static String DATADOG_CONFIG_SECTION_PATH = "tpsReport.dataDog";
    public String host;
    public int port;
    public boolean enabled;

    public DataDogConfiguration(Configuration config) {
        super(config);
        ConfigurationSection ddSection = config.getConfigurationSection(DATADOG_CONFIG_SECTION_PATH);
        if (ddSection == null) {
            Bukkit.getLogger().warning("No data dog configuration found, including defaults. Misconfigured somehow?");
            return;
        }
        enabled = ddSection.getBoolean("enabled");
        host = ddSection.getString("host");
        port = ddSection.getInt("port");
    }
}
