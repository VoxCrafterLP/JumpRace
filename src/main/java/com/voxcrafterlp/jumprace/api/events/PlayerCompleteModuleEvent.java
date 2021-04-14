package com.voxcrafterlp.jumprace.api.events;

import com.voxcrafterlp.jumprace.modules.objects.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 19.03.2021
 * Time: 16:19
 * Project: JumpRace
 */

public class PlayerCompleteModuleEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Module module;

    public PlayerCompleteModuleEvent(Player player, Module module) {
        this.player = player;
        this.module = module;
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

    public Module getModule() {
        return module;
    }
}
