package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.03.2021
 * Time: 20:55
 * Project: JumpRace
 */

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta().getDisplayName() == null) return;

        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.LOBBY) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Player player = event.getPlayer();

        if(event.getItem().getItemMeta().getDisplayName().equals("§bTeam Selector")) {
            player.openInventory(JumpRace.getInstance().getInventoryManager().getTeamSelectorInventory());
            player.playSound(player.getLocation(), Sound.CHEST_OPEN,1,1);
            return;
        }
        if(event.getItem().getItemMeta().getDisplayName().equals("§cLeave")) {
            player.kickPlayer(JumpRace.getInstance().getPrefix() + "§7You §bleft §7the game§8.");
            return;
        }
    }

}
