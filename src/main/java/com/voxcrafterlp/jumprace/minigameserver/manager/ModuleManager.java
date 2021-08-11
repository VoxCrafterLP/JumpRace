package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.exceptions.ModuleNotFoundException;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.objects.ModuleRow;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
    private int moduleLength;

    public ModuleManager() {
        this.loadedModules = JumpRace.getInstance().getModuleLoader().getModuleList();
        this.selectedModules = Lists.newCopyOnWriteArrayList();
        this.moduleRows = Lists.newCopyOnWriteArrayList();
        this.moduleLength = 0;
    }

    /**
     * Calculate the necessary ModuleRow amount and build them
     */
    public void buildModuleRows() {
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


        this.selectedModules.forEach(module -> this.moduleLength = moduleLength + module.getModuleLength());
        Bukkit.getConsoleSender().sendMessage("§aModules built successfully");
    }

    /**
     * Pick random modules of a certain difficulty
     * @param moduleDifficulty Difficultly of the modules
     * @param amount The amount of modules which should be picked
     * @return Array containing the picked modules
     */
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
     * @see #pickRandomModules(ModuleDifficulty, int)
     * @throws ModuleNotFoundException If not enough modules of the specified difficulty could be found
     */
    private void fillList(List<Module> list, ModuleDifficulty moduleDifficulty, int amount) throws ModuleNotFoundException {
        final Module[] newModules = this.pickRandomModules(moduleDifficulty, amount);

        if(newModules.length == 0)
            throw new ModuleNotFoundException("The server couldn't find enough modules to build the map!");

        list.addAll(Arrays.asList(newModules));
    }

    public ModuleRow getModuleRow(Player player) {
        return this.moduleRows.stream()
                .filter(moduleRow -> moduleRow.getPlayer() != null)
                .filter(moduleRow -> moduleRow.getPlayer().equals(player))
                .findFirst().get();
    }

}
