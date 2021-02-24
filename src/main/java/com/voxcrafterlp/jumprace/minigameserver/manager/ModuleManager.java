package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.exceptions.ModuleNotFoundException;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.ExceptionPlayerNotFound;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 23.02.2021
 * Time: 16:26
 * Project: JumpRace
 */

@Getter
public class ModuleManager {

    private final List<Module> loadedModules;

    public ModuleManager() {
        this.loadedModules = JumpRace.getInstance().getModuleLoader().getModuleList();
    }

    public void buildModules() {
        if(this.loadedModules.isEmpty()) return;

        final Module[] selectedModules = new Module[10];
        try {
            this.fillArray(selectedModules, ModuleDifficulty.EASY, 4);
            this.fillArray(selectedModules, ModuleDifficulty.NORMAL, 3);
            this.fillArray(selectedModules, ModuleDifficulty.HARD, 2);
            this.fillArray(selectedModules, ModuleDifficulty.VERY_HARD, 1);
        } catch (ModuleNotFoundException exception) {
            exception.printStackTrace();
        }

        //final int rows = JumpRace.getInstance().getJumpRaceConfig().getTeamAmount() * JumpRace.getInstance().getJumpRaceConfig().getTeamSize();
        int rows = 1;
        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger taskID = new AtomicInteger();

        taskID.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(JumpRace.getInstance(), () -> {
            int z = i.get() * 100;
            this.buildModuleRow(z, selectedModules);
            i.getAndIncrement();

            if(i.get() == (rows - 1))
                Bukkit.getScheduler().cancelTask(taskID.get());
        }, 10, 10));
    }

    private void buildModuleRow(final int z, Module[] selectedModules) {
        int spawnedModules = 0;
        final int height = JumpRace.getInstance().getJumpRaceConfig().getModuleSpawnHeight();
        Location lastEndPoint = null;

        for(Module module : selectedModules) {
            if(spawnedModules == 0)
                module.build(new Location(Bukkit.getWorld("jumprace"), 0, height, z), false);
            else
                module.build(this.calculateSpawnLocation(lastEndPoint, module.getStartPoint()), spawnedModules == 9);

            lastEndPoint = module.getEndPointLocation();
            spawnedModules++;
        }
    }

    private Module[] pickRandomModules(ModuleDifficulty moduleDifficulty, int amount) {
        List<Module> list = Lists.newCopyOnWriteArrayList();

        this.loadedModules.forEach(module -> {
            if(module.getModuleDifficulty().equals(moduleDifficulty))
                list.add(module);
        });

        Collections.shuffle(list);
        Module[] array = new Module[amount];

        for (int i = 0; i<amount; i++) {
            if(list.size() >= (i + 1))
                array[i] = list.get(i);
            else
                array[i] = list.get(new Random().nextInt(list.size()));
        }

        return array;
    }

    /**
     * Calculates the spawn location of the module based on the last module
     * @param endPoint Endpoint location of the last module
     * @param relativePosition RelativePosition of the module
     * @return Spawn location of the module
     */
    private Location calculateSpawnLocation(Location endPoint, RelativePosition relativePosition) {
        int x = endPoint.getBlockX() - relativePosition.getRelativeX();
        int y = endPoint.getBlockY() - relativePosition.getRelativeY();
        int z = endPoint.getBlockZ() - relativePosition.getRelativeZ();

        return new Location(Bukkit.getWorld("jumprace"), x, y, z);
    }

    private void fillArray(Module[] array, ModuleDifficulty moduleDifficulty, int amount) throws ModuleNotFoundException{
        final Module[] newModules = this.pickRandomModules(moduleDifficulty, amount);

        if(newModules.length == 0)
            throw new ModuleNotFoundException("The server couldn't find enough modules to build the map!");

        for(int i = 0; i<newModules.length; i++)
            Array.set(array, i, newModules[i]);
    }

}
