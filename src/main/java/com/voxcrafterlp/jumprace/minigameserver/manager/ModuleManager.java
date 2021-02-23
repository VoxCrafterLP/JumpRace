package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.*;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 23.02.2021
 * Time: 16:26
 * Project: JumpRace
 */

@Getter
public class ModuleManager {

    private final List<Module> loadedModules;
    private final Map<ModuleDifficulty, Module> modules;

    public ModuleManager() {
        this.loadedModules = JumpRace.getInstance().getModuleLoader().getModuleList();
        this.modules = new HashMap<>();
        this.loadedModules.forEach(module -> modules.put(module.getModuleDifficulty(), module));
    }

    public void buildModules() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () -> {

        }, 10, 10);
    }

    private Module[] pickRandomModules(ModuleDifficulty moduleDifficulty, int amount) {
        List<Module> list = Lists.newCopyOnWriteArrayList();

        this.modules.forEach((difficulty, module) -> {
            if(difficulty.equals(moduleDifficulty))
                list.add(module);
        });

        Collections.shuffle(list);
        Module[] array = new Module[amount];

        for (int i = 0; i<amount; i++) {
            if(list.get(i) != null)
                array[i] = list.get(i);
            else
                array[i] = list.get(new Random().nextInt(list.size()));
        }

        return array;
    }

}
