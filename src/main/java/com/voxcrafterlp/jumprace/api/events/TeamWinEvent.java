package com.voxcrafterlp.jumprace.api.events;

import com.voxcrafterlp.jumprace.objects.Team;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 19.03.2021
 * Time: 15:23
 * Project: JumpRace
 */

@Getter
public class TeamWinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Team winningTeam;

    public TeamWinEvent(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
