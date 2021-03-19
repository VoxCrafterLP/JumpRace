package com.voxcrafterlp.jumprace;

import com.voxcrafterlp.jumprace.builderserver.commands.JumpRaceCommand;
import com.voxcrafterlp.jumprace.builderserver.listener.*;
import com.voxcrafterlp.jumprace.builderserver.listener.editor.EditorSetupListener;
import com.voxcrafterlp.jumprace.builderserver.listener.editor.PlayerModifyBarrierListener;
import com.voxcrafterlp.jumprace.builderserver.listener.editor.Protection;
import com.voxcrafterlp.jumprace.config.JumpRaceConfig;
import com.voxcrafterlp.jumprace.config.LanguageLoader;
import com.voxcrafterlp.jumprace.minigameserver.listener.EnchantmentListener;
import com.voxcrafterlp.jumprace.minigameserver.listener.PlayerLoginListener;
import com.voxcrafterlp.jumprace.minigameserver.listener.PlayerMoveListener;
import com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch.EntityDamageByEntityListener;
import com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch.InstantTNTListener;
import com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch.PlayerDeathListener;
import com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch.PlayerRespawnListener;
import com.voxcrafterlp.jumprace.minigameserver.manager.GameManager;
import com.voxcrafterlp.jumprace.minigameserver.manager.InventoryManager;
import com.voxcrafterlp.jumprace.minigameserver.manager.LocationManager;
import com.voxcrafterlp.jumprace.minigameserver.commands.SetupCommand;
import com.voxcrafterlp.jumprace.minigameserver.manager.ModuleManager;
import com.voxcrafterlp.jumprace.minigameserver.setup.listener.SetupListener;
import com.voxcrafterlp.jumprace.minigameserver.setup.objects.MapSetup;
import com.voxcrafterlp.jumprace.modules.utils.ModuleLoader;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import lombok.Getter;
import org.bukkit.*;
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
    private LanguageLoader languageLoader;

    private HashMap<Player, ModuleEditor> editorSessions;
    private HashMap<Player, MapSetup> mapSetups;

    private GameManager gameManager;
    private ModuleManager moduleManager;
    private LocationManager locationManager;
    private InventoryManager inventoryManager;

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

        this.loadWorld();
        this.editorSessions = new HashMap<>();
        this.loadDefaultModule();
        this.loadModules();
    }

    private void minigameServerStartup() {
        Bukkit.getConsoleSender().sendMessage("§7Starting server as a §aminigame §7server.");
        Bukkit.getConsoleSender().sendMessage(" ");

        getCommand("setup").setExecutor(new SetupCommand());

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new com.voxcrafterlp.jumprace.minigameserver.listener.PlayerJoinListener(), this);
        pluginManager.registerEvents(new com.voxcrafterlp.jumprace.minigameserver.listener.PlayerQuitListener(), this);
        pluginManager.registerEvents(new SetupListener(), this);
        pluginManager.registerEvents(new PlayerLoginListener(), this);
        pluginManager.registerEvents(new com.voxcrafterlp.jumprace.minigameserver.listener.Protection(), this);
        pluginManager.registerEvents(new com.voxcrafterlp.jumprace.minigameserver.listener.InventoryClickListener(), this);
        pluginManager.registerEvents(new com.voxcrafterlp.jumprace.minigameserver.listener.PlayerInteractListener(), this);
        pluginManager.registerEvents(new PlayerMoveListener(), this);

        pluginManager.registerEvents(new PlayerDeathListener(), this);
        pluginManager.registerEvents(new PlayerRespawnListener(), this);
        pluginManager.registerEvents(new com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch.PlayerInteractListener(), this);
        pluginManager.registerEvents(new com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch.InventoryClickListener(), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(), this);
        pluginManager.registerEvents(new InstantTNTListener(), this);
        pluginManager.registerEvents(new EnchantmentListener(), this);

        this.loadWorld();
        this.mapSetups = new HashMap<>();
        this.loadDefaultModule();
        this.loadModules();

        this.gameManager = new GameManager();
        this.moduleManager = new ModuleManager();
        this.moduleManager.buildModules();
        this.locationManager = new LocationManager();
        this.inventoryManager = new InventoryManager();

        if(this.locationManager.getLobbyLocation() != null) {
            Bukkit.getWorld(this.locationManager.getLobbyLocation().getWorld().getName()).setGameRuleValue("doDaylightCycle", "false");
            Bukkit.getWorld(this.locationManager.getLobbyLocation().getWorld().getName()).setDifficulty(Difficulty.PEACEFUL);
        }
    }

    /**
     * Load the main world
     */
    private void loadWorld() {
        if(Bukkit.getWorld("jumprace") == null) {
            Bukkit.getConsoleSender().sendMessage("§aGenerating JumpRace world...");
            WorldCreator worldCreator = new WorldCreator("jumprace");
            worldCreator.environment(World.Environment.NORMAL);
            worldCreator.type(WorldType.FLAT);
            worldCreator.generatorSettings("0");
            worldCreator.generateStructures(false);
            worldCreator.createWorld();

            Bukkit.getWorld("jumprace").setGameRuleValue("doDaylightCycle", "false");
            Bukkit.getWorld("jumprace").setDifficulty(Difficulty.PEACEFUL);
        }
    }

    /**
     * Load the default config
     */
    private void loadConfig() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        this.jumpRaceConfig = new JumpRaceConfig();
        this.loadLanguages();
    }

    /**
     * Load the default languages into the languages folder
     */
    private void loadLanguages() {
        new File("plugins/JumpRace/languages/").mkdir();

        this.saveResource("languages/en_US.yml", false);
        this.saveResource("languages/de_DE.yml", false);

        this.languageLoader = new LanguageLoader("plugins/JumpRace/languages/" + this.jumpRaceConfig.getLanguageFile() + ".yml");
    }

    private void loadModules() {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§7Loading modules..");

        try {
            this.moduleLoader = new ModuleLoader();
            this.moduleLoader.loadModules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Export default module to the modules folder
     */
    private void loadDefaultModule() {
        new File("plugins/JumpRace/modules/").mkdir();
        if(new File("plugins/JumpRace/modules/default").mkdir()) {
            JumpRace.getInstance().saveResource("modules/default/module.json", false);
            JumpRace.getInstance().saveResource("modules/default/module.schematic", false);
        }
    }

    public String getPrefix() {
        return this.languageLoader.getTranslationByKey("prefix");
    }

    public static JumpRace getInstance() {
        return instance;
    }

}
