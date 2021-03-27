package com.voxcrafterlp.jumprace.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 19.03.2021
 * Time: 15:19
 * Project: JumpRace
 */

public class PlayerReachGoalEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public PlayerReachGoalEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }
}
