package haus.gamer.mc.tpsreport.configuration;

import org.bukkit.configuration.Configuration;

import java.util.List;

public class GlobalConfiguration extends ConfigParser {
    public DataDogConfiguration ddConfig;
    public List<String> alwaysAddedTags;

    public GlobalConfiguration(Configuration configuration) {
        super(configuration);
        ddConfig = new DataDogConfiguration(configuration);
        alwaysAddedTags = configuration.getStringList("alwaysAddedTags");
    }
}
