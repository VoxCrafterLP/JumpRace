package com.voxcrafterlp.jumprace.api.events;

import com.voxcrafterlp.jumprace.modules.objects.Module;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 19.03.2021
 * Time: 15:11
 * Project: JumpRace
 */

public class ModuleFailEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Module module;
    private final Location respawnLocation;

    public ModuleFailEvent(Player player, Module module, Location respawnLocation) {
        this.player = player;
        this.module = module;
        this.respawnLocation = respawnLocation;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getRespawnLocation() {
        return respawnLocation;
    }

    public Module getModule() {
        return module;
    }
}
