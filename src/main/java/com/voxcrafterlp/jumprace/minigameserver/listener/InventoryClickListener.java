package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.minigameserver.scoreboard.PlayerScoreboard;
import com.voxcrafterlp.jumprace.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.03.2021
 * Time: 18:14
 * Project: JumpRace
 */

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getInventory() == null) return;
        if(event.getInventory().getName() == null) return;
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        final Player player = (Player) event.getWhoClicked();

        if(event.getInventory().getName().equalsIgnoreCase(JumpRace.getInstance().getInventoryManager().getTeamSelectorInventory().getName())) {
            event.setCancelled(true);
            if(event.getCurrentItem().getType() != Material.LEATHER_BOOTS) return;

            final Team team = JumpRace.getInstance().getGameManager().getRegisteredTeams().get((event.getSlot() - 9));

            if(team.getMembers().contains(player)) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS,2,2);
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-already-in-team"));
                return;
            }

            if(team.isFull()) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS,2,2);
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("message-team-full"));
                return;
            }

            final Team oldTeam = JumpRace.getInstance().getGameManager().getTeamFromPlayer(player);
            if(oldTeam != null)
                oldTeam.getMembers().remove(player);

            team.getMembers().add(player);
            JumpRace.getInstance().getInventoryManager().updateTeamSelectorInventory();
            player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-join-team-success", team.getTeamColor().getTeamNameWithColorCode()));
            player.closeInventory();

            Bukkit.getOnlinePlayers().forEach(players -> new PlayerScoreboard().setScoreboard(players));
            return;
        }

        if(event.getInventory().getName().equalsIgnoreCase(JumpRace.getInstance().getInventoryManager().getMapSwitcherInventory().getName())) {
            event.setCancelled(true);

            if(event.getSlot() == 53)
                JumpRace.getInstance().getLocationManager().pickRandomMap();
            else
                JumpRace.getInstance().getLocationManager().setMap(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

            player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);
            player.closeInventory();
        }

        if(event.getInventory().getName().equalsIgnoreCase(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("inventory-spectator-teleport-name"))) {
            final Player target = Bukkit.getPlayer(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
            if(target == null) {
                player.closeInventory();
                player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("player-not-found"));
                return;
            }
            player.closeInventory();
            player.teleport(target.getLocation());
            player.playSound(player.getLocation(), Sound.WOOD_CLICK,10,10);
            return;
        }
    }
}
