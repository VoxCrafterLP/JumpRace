package com.voxcrafterlp.jumprace.minigameserver.listener.deathmatch;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 14.03.2021
 * Time: 10:15
 * Project: JumpRace
 */

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.DEATHMATCH) return;
        final Player player = event.getEntity();

        JumpRace.getInstance().getGameManager().removeLive(player, event.getDrops());

        event.setKeepInventory(true);
        event.setDroppedExp(0);

        if(event.getEntity().getKiller() != null)
            event.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 140, 1));

        ((CraftPlayer) event.getEntity()).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));

        event.setDeathMessage(JumpRace.getInstance().getPrefix() + ((event.getEntity().getKiller() == null) ?
                JumpRace.getInstance().getGameManager().getPlayerNames().get(player) + " §7died§8." :
                JumpRace.getInstance().getGameManager().getPlayerNames().get(player) + " §7has been §ckilled §7by " +
                JumpRace.getInstance().getGameManager().getPlayerNames().get(player.getKiller()) + "§8."));
    }

}
