package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.api.events.ModuleFailEvent;
import com.voxcrafterlp.jumprace.api.events.PlayerReachGoalEvent;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.08.2021
 * Time: 18:55
 * Project: JumpRace
 */

public class PlayerReachGoalListener implements Listener {

    private static final HashMap<Player, Integer> fails = new HashMap<>();

    @EventHandler
    public void onFail(ModuleFailEvent event) {
        final Player player = event.getPlayer();

        if(fails.containsKey(player))
            fails.replace(player, fails.get(player) + 1);
        else
            fails.put(player, 1);
    }

    @EventHandler
    public void onReach(PlayerReachGoalEvent event) {
        final Player player = event.getPlayer();

        if(!fails.containsKey(player))
            player.getInventory().setHelmet(new ItemManager(Material.DIAMOND_HELMET).setUnbreakable(true).build());
    }
}
