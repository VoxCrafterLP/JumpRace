package com.voxcrafterlp.jumprace.modules.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.utils.CalculatorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private final List<Location> enderChestLocations;
    private final int z;
    private Player player;

    public ModuleRow(List<Module> modules, int z) {
        this.z = z;
        this.modules = Lists.newCopyOnWriteArrayList();
        this.enderChestLocations = Lists.newCopyOnWriteArrayList();
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

        this.modules.forEach(module -> this.enderChestLocations.add(module.getEnderChestLocation()));
    }

    public ModuleRow assignPlayer(Player player) {
        this.player = player;
        this.player.teleport(this.getModules().get(0).getPlayerSpawnLocation());

        AtomicInteger i = new AtomicInteger(1);
        this.modules.forEach(module -> {
            module.spawnHologram(player, i.get());
            i.getAndIncrement();
        });

        return this;
    }


}
