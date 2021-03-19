package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.minigameserver.scoreboard.PlayerScoreboard;
import com.voxcrafterlp.jumprace.modules.objects.ModuleRow;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 18.03.2021
 * Time: 10:05
 * Project: JumpRace
 */

@Getter
public class SpectatorManager {

    private final List<Player> spectators;

    public SpectatorManager() {
        this.spectators = Lists.newCopyOnWriteArrayList();
    }

    public boolean isSpectator(Player player) {
        return spectators.contains(player);
    }

    public void setSpectating(Player player) {
        if(!this.spectators.contains(player))
            this.spectators.add(player);
        JumpRace.getInstance().getGameManager().getPlayersLeft().remove(player);
        JumpRace.getInstance().getGameManager().getLivesLeft().remove(player);

        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-spectator"));

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2));

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!this.isSpectator(players)) {
                Bukkit.getOnlinePlayers().forEach(all -> {
                    if(this.isSpectator(all))
                        players.hidePlayer(all);
                });
            }
            new PlayerScoreboard().setScoreboard(players);
        });

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setFireTicks(0);
        player.spigot().setCollidesWithEntities(false);
        player.getInventory().setItem(0, new ItemManager(Material.COMPASS).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("hotbar-item-spectator-teleport-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("hotbar-item-spectator-teleport-description")).build());
        player.getInventory().setItem(8, new ItemManager(Material.MAGMA_CREAM).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("hotbar-item-leave-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("hotbar-item-leave-description")).build());

        this.teleport(player);
    }

    public void removeSpectator(Player player) {
        this.spectators.remove(player);
    }

    private void teleport(Player player) {
        switch (JumpRace.getInstance().getGameManager().getGameState()) {
            case JUMPING:
                player.teleport((JumpRace.getInstance().getGameManager().getModuleRows().values().toArray(new ModuleRow[]{})[0]).getModules().get(0).getPlayerSpawnLocation());
                break;
            case DEATHMATCH:
                player.teleport(JumpRace.getInstance().getLocationManager().getSelectedMap().getRandomSpawnLocation());
                break;
            case LOBBY:
            case ENDING:
                player.teleport(JumpRace.getInstance().getLocationManager().getLobbyLocation());
                break;
        }
    }
}
