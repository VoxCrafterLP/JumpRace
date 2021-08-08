package com.voxcrafterlp.jumprace.minigameserver.setup.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.objects.Map;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.02.2021
 * Time: 17:11
 * Project: JumpRace
 */

@Getter
public class MapSetup {

    private final Player player;
    private final String name;
    private final List<Location> spawnLocations, endPointLocations;

    public MapSetup(Player player, String name) {
        this.player = player;
        this.name = name;
        this.spawnLocations = Lists.newCopyOnWriteArrayList();
        this.endPointLocations = Lists.newCopyOnWriteArrayList();

        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-map-instructions-1"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-map-instructions-2"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-map-instructions-3"));
        JumpRace.getInstance().getMapSetups().put(this.player, this);
    }

    public void addSpawnLocation(Location location) {
        this.spawnLocations.add(location);
    }

    public void addEndPointLocation(Location location) {
        this.endPointLocations.add(location.clone().getBlock().getLocation().add(0.0, -1.0, 0.0));
    }

    /**
     * Checks if the map has been configured correctly and gives the player feedback.
     * If so, the map will be added to the loaded maps list.
     * @return Returns if the map has been configured correctly
     */
    public boolean finish() {
        if(spawnLocations.isEmpty()) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-error-no-spawnpoint"));
            return false;
        }
        if(endPointLocations.isEmpty()) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-error-no-endpoint"));
            return false;
        }

        JumpRace.getInstance().getMapSetups().remove(this.player);
        JumpRace.getInstance().getLocationManager().getLoadedMaps()
                .add(new Map(this.name, this.spawnLocations, this.endPointLocations));
        return true;
    }

}
