package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.objects.Team;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 17.03.2021
 * Time: 08:30
 * Project: JumpRace
 */

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.DEATHMATCH) return;
        if(event.getEntityType() != EntityType.PLAYER) return;
        if(event.getDamager().getType() != EntityType.PLAYER && event.getDamager().getType() != EntityType.ARROW) return;

        final Player player = (Player) event.getEntity();
        final Player damager = ((event.getDamager().getType() == EntityType.PLAYER) ? (Player) event.getDamager() : (Player) ((Arrow) event.getDamager()).getShooter());

        if(JumpRace.getInstance().getGameManager().getSpectatorManager().isSpectator(damager)) {
            event.setCancelled(true);
            return;
        }

        final Team team = JumpRace.getInstance().getGameManager().getTeamFromPlayer(player);

        if(team == null) {
            event.setCancelled(true);
            return;
        }

        if(team.getMembers().contains(damager))
            event.setDamage(0.0);

    }
}
