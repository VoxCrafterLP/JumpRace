package com.voxcrafterlp.jumprace.modules.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.api.events.ModuleFailEvent;
import com.voxcrafterlp.jumprace.api.events.PlayerCompleteModuleEvent;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.utils.CalculatorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.03.2021
 * Time: 21:15
 * Project: JumpRace
 */

@Getter
public class ModuleRow {

    private final List<Module> modules;
    private final int z;
    private Player player;
    private Location respawnLocation;
    private int modulesCompleted, taskID, respawnHeight;

    public ModuleRow(List<Module> modules, int z) {
        this.z = z;
        this.modules = Lists.newCopyOnWriteArrayList();
        this.build(modules);
    }

    /**
     * Build the modules in a row
     * @param modules List containing the modules which should be built
     */
    private void build(List<Module> modules) {
        AtomicInteger spawnedModules = new AtomicInteger(0);
        final int height = JumpRace.getInstance().getJumpRaceConfig().getModuleSpawnHeight();
        AtomicReference<Location> lastEndPoint = new AtomicReference<>();

        modules.forEach(module -> {
            final Module newModule = module.clone();

            if(spawnedModules.get() == 0)
                newModule.build(new Location(Bukkit.getWorld("jumprace"), 0, height, z), false);
            else
                newModule.build(new CalculatorUtil().calculateSpawnLocation(lastEndPoint.get(), newModule.getStartPoint()), spawnedModules.get() == 9);

            lastEndPoint.set(newModule.getEndPointLocation().clone());
            this.modules.add(newModule);
            spawnedModules.getAndIncrement();
        });
    }

    /**
     * Teleport the player to the first module's spawn location and start the respawn scheduler.
     * @param player Player which should be assigned
     * @return Current instance of the ModuleRow
     */
    public ModuleRow assignPlayer(Player player) {
        this.player = player;
        final Location location = this.getModules().get(0).getPlayerSpawnLocation();
        this.respawnLocation = location;
        this.player.teleport(location);
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.setExp(0);
        this.player.setLevel(0);
        this.modulesCompleted = 0;
        this.respawnHeight = this.getModules().get(0).getModuleBorders()[0].getBlockY();

        this.modules.get(0).spawnHologram(player, 1);
        this.modules.get(1).spawnHologram(player, 2);
        this.startRespawnScheduler();

        return this;
    }

    /**
     * Teleport the player to the start point of the current module
     */
    public void respawn() {
        player.teleport(this.respawnLocation);
        player.playSound(player.getLocation(), Sound.NOTE_BASS,1,1);
        Bukkit.getPluginManager().callEvent(new ModuleFailEvent(this.player, this.modules.get(this.modulesCompleted), this.respawnLocation));
    }

    /**
     * Check if the module of the gold plate has already been completed.
     * If not, completes the module.
     * @param location Location of the gold plate
     */
   public void triggerGoldPlate(Location location) {
        if(this.modulesCompleted == 10) return;
        if(!location.equals(this.modules.get(this.modulesCompleted).getGoldPlateLocation())) return;

        Bukkit.getPluginManager().callEvent(new PlayerCompleteModuleEvent(player, this.modules.get(this.modulesCompleted)));

        this.modulesCompleted++;
        player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-module-complete", String.valueOf(this.modulesCompleted)));

        final int nextModule = this.modulesCompleted + 1;
        if(nextModule < 10)
            this.modules.get(nextModule).spawnHologram(player, this.modulesCompleted + 2);

        if(this.modulesCompleted == 10) {
            JumpRace.getInstance().getGameManager().reachGoal(player);
            return;
        }

        this.respawnLocation = this.modules.get(this.modulesCompleted).getPlayerSpawnLocation();
        this.respawnHeight = this.getModules().get(this.modulesCompleted).getModuleBorders()[0].getBlockY();
   }

    /**
     * Get the current ModuleDifficulty based on the completed modules
     * @return Calculated ModuleDifficulty
     */
   public ModuleDifficulty getCurrentModuleDifficulty() {
        if(this.modulesCompleted < 4)
            return ModuleDifficulty.EASY;
        else if(this.modulesCompleted < 7)
            return ModuleDifficulty.NORMAL;
        else if(this.modulesCompleted < 9)
            return ModuleDifficulty.HARD;
        else
            return ModuleDifficulty.VERY_HARD;
   }

   private void startRespawnScheduler() {
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () -> {
            if(player.getLocation().getBlockY() < this.respawnHeight)
                this.respawn();
        }, 5, 1);
   }

   public void stopRespawnScheduler() {
        Bukkit.getScheduler().cancelTask(this.taskID);
   }

}
