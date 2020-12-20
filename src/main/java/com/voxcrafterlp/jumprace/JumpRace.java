package com.voxcrafterlp.jumprace;

import com.voxcrafterlp.jumprace.builderserver.listener.BuilderPlayerJoinListener;
import com.voxcrafterlp.jumprace.config.JumpRaceConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.12.2020
 * Time: 23:22
 * Project: JumpRace
 */

@Getter
public class JumpRace extends JavaPlugin {

    private static JumpRace instance;

    private JumpRaceConfig jumpRaceConfig;

    @Override
    public void onEnable() {
        instance = this;
        this.loadConfig();

        Bukkit.getConsoleSender().sendMessage("§8=======================================");

        if(this.jumpRaceConfig.isBuilderServer())
            this.builderServerStartup();
        else
            this.minigameServerStartup();

        Bukkit.getConsoleSender().sendMessage("§8=======================================");
    }

    private void builderServerStartup() {
        Bukkit.getConsoleSender().sendMessage("§7Starting server as a §abuilder §7server.");

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BuilderPlayerJoinListener(), this);
    }

    private void minigameServerStartup() {
        Bukkit.getConsoleSender().sendMessage("§7Starting server as a §aminigame §7server.");
    }

    /**
     * Loads the default config
     */
    private void loadConfig() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        this.jumpRaceConfig = new JumpRaceConfig();
    }

    private void loadModules() {
        Bukkit.getConsoleSender().sendMessage("§7Loading modules..");
        new File("plugins/JumpRace/modules/").mkdir();

    }

    public String getPrefix() { return "§8[§bJumpRace§8] "; }

    public static JumpRace getInstance() { return instance; }

}
