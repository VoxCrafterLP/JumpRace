package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.exceptions.ModuleNotFoundException;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.objects.ModuleRow;
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
    private final List<Module> selectedModules;
    private final List<ModuleRow> moduleRows;

    public ModuleManager() {
        this.loadedModules = JumpRace.getInstance().getModuleLoader().getModuleList();
        this.selectedModules = Lists.newCopyOnWriteArrayList();
        this.moduleRows = Lists.newCopyOnWriteArrayList();
    }

    public void buildModules() {
        if(this.loadedModules.isEmpty()) return;

        try {
            this.fillList(this.selectedModules, ModuleDifficulty.EASY, 4);
            this.fillList(this.selectedModules, ModuleDifficulty.NORMAL, 3);
            this.fillList(this.selectedModules, ModuleDifficulty.HARD, 2);
            this.fillList(this.selectedModules, ModuleDifficulty.VERY_HARD, 1);
        } catch (ModuleNotFoundException exception) {
            exception.printStackTrace();
        }

        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("Building modules..");

        final int rows = JumpRace.getInstance().getJumpRaceConfig().getTeamAmount() * JumpRace.getInstance().getJumpRaceConfig().getTeamSize();
        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger taskID = new AtomicInteger();

        taskID.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(JumpRace.getInstance(), () -> {
            int z = i.get() * 100;
            this.moduleRows.add(new ModuleRow(this.selectedModules, z));
            i.getAndIncrement();

            if(i.get() == rows)
                Bukkit.getScheduler().cancelTask(taskID.get());
        }, 10, 10));

        Bukkit.getConsoleSender().sendMessage("§aModules built successfully");
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

    private void fillList(List<Module> list, ModuleDifficulty moduleDifficulty, int amount) throws ModuleNotFoundException {
        final Module[] newModules = this.pickRandomModules(moduleDifficulty, amount);

        if(newModules.length == 0)
            throw new ModuleNotFoundException("The server couldn't find enough modules to build the map!");

        list.addAll(Arrays.asList(newModules));
    }

}
