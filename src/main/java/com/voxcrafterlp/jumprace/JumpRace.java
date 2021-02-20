package com.voxcrafterlp.jumprace;

import com.voxcrafterlp.jumprace.builderserver.commands.JumpRaceCommand;
import com.voxcrafterlp.jumprace.builderserver.listener.*;
import com.voxcrafterlp.jumprace.builderserver.listener.editor.EditorSetupListener;
import com.voxcrafterlp.jumprace.builderserver.listener.editor.PlayerModifyBarrierListener;
import com.voxcrafterlp.jumprace.builderserver.listener.editor.Protection;
import com.voxcrafterlp.jumprace.config.JumpRaceConfig;
import com.voxcrafterlp.jumprace.modules.utils.ModuleLoader;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
    private ModuleLoader moduleLoader;
    private HashMap<Player, ModuleEditor> editorSessions = new HashMap<>();

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
        Bukkit.getConsoleSender().sendMessage(" ");

        getCommand("jumprace").setExecutor(new JumpRaceCommand());
        getCommand("jr").setExecutor(new JumpRaceCommand());

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new AsyncPlayerChatListener(), this);
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);

        pluginManager.registerEvents(new Protection(), this);
        pluginManager.registerEvents(new PlayerModifyBarrierListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new EditorSetupListener(), this);

        if(Bukkit.getWorld("jumprace") == null) {
            Bukkit.getConsoleSender().sendMessage("§aGenerating JumpRace world...");
            WorldCreator worldCreator = new WorldCreator("jumprace");
            worldCreator.environment(World.Environment.NORMAL);
            worldCreator.type(WorldType.FLAT);
            worldCreator.generatorSettings("0");
            worldCreator.generateStructures(false);
            worldCreator.createWorld();

            Bukkit.getWorld("jumprace").setGameRuleValue("doDaylightCycle", "false");
        }

        this.loadDefaultModule();
        this.loadModules();
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

        try {
            this.moduleLoader = new ModuleLoader(this.jumpRaceConfig.isBuilderServer());
            this.moduleLoader.loadModules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultModule() {
        new File("plugins/JumpRace/modules/").mkdir();
        if(new File("plugins/JumpRace/modules/default").mkdir()) {
            JumpRace.getInstance().saveResource("modules/default/module.json", false);
            JumpRace.getInstance().saveResource("modules/default/module.schematic", false);
        }
    }

    public String getPrefix() { return "§8[§bJumpRace§8] "; }

    public static JumpRace getInstance() { return instance; }

}
