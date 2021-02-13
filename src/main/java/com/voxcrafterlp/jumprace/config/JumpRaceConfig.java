package com.voxcrafterlp.jumprace.config;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.12.2020
 * Time: 23:31
 * Project: JumpRace
 */

@Getter
public class JumpRaceConfig {

    private final File configFile;
    private final YamlConfiguration configuration;

    private final boolean builderServer;
    private final int moduleSpawnHeight;

    public JumpRaceConfig() {
        this.configFile = new File("plugins/JumpRace/config.yml");
        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);

        this.builderServer = this.configuration.getBoolean("builder-server");
        this.moduleSpawnHeight = this.configuration.getInt("module-spawn-height");
    }

}
