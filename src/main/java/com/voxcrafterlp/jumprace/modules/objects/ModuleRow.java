package com.voxcrafterlp.jumprace.modules.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.utils.CalculatorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
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

    public ModuleRow assignPlayer(Player player) {
        this.player = player;
        final Location location = this.getModules().get(0).getPlayerSpawnLocation();
        this.respawnLocation = location;
        this.player.teleport(location);
        this.modulesCompleted = 0;
        this.respawnHeight = this.getModules().get(0).getModuleBorders()[0].getBlockY();

        AtomicInteger i = new AtomicInteger(1);
        this.modules.forEach(module -> {
            module.spawnHologram(player, i.get());
            i.getAndIncrement();
        });

        this.startRespawnScheduler();

        return this;
    }

    public void respawn() {
        player.teleport(this.respawnLocation);
    }

   public void triggerGoldPlate(Location location) {
       if(this.modulesCompleted == 10) return;

        if(!location.equals(this.modules.get(this.modulesCompleted).getGoldPlateLocation())) return;

        this.modulesCompleted++;
        player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You completed §bmodule " + this.modulesCompleted + "§8.");

       if(this.modulesCompleted == 10) {
           JumpRace.getInstance().getGameManager().reachGoal(player);
           return;
       }

        this.respawnLocation = this.modules.get(this.modulesCompleted).getPlayerSpawnLocation();
       this.respawnHeight = this.getModules().get(this.modulesCompleted).getModuleBorders()[0].getBlockY();
   }

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

   private void stopRespawnScheduler() {
        Bukkit.getScheduler().cancelTask(this.taskID);
   }

}
