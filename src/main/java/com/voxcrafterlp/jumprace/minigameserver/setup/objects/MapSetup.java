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
    private final List<Location> spawnLocations;

    public MapSetup(Player player, String name) {
        this.player = player;
        this.name = name;
        this.spawnLocations = Lists.newCopyOnWriteArrayList();

        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Type §b\"add\" §7to add a new §bspawn location§8.");
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Type §b\"finish\" §7to §bfinish §7the setup§8.");
        JumpRace.getInstance().getMapSetups().put(this.player, this);
    }

    public void addSpawnLocation(Location location) {
        this.spawnLocations.add(location);
    }

    public Map finish() {
        JumpRace.getInstance().getMapSetups().remove(this.player);
        return new Map(this.name, this.spawnLocations);
    }

}
