package com.voxcrafterlp.jumprace.minigameserver.manager;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.minigameserver.scoreboard.PlayerScoreboard;
import com.voxcrafterlp.jumprace.objects.Map;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.02.2021
 * Time: 16:05
 * Project: JumpRace
 */

@Getter @Setter
public class LocationManager {

    private final List<Map> loadedMaps;
    private Location lobbyLocation;
    private Map selectedMap;

    private final File locationFile;
    private final YamlConfiguration configuration;

    public LocationManager() {
        this.loadedMaps = Lists.newCopyOnWriteArrayList();

        this.locationFile = new File("plugins/JumpRace/locations.yml");
        try {
            this.locationFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.locationFile);

        this.loadData();
    }

    public void loadData() {
        this.loadedMaps.clear();

        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("Loading maps..");

        if(configuration.contains("lobby"))
            this.lobbyLocation = this.getLocationFromJSONObject(new JSONObject(this.configuration.getString("lobby")));

        if(configuration.contains("maps"))
            this.configuration.getStringList("maps").forEach(string -> {
                final Map map = new Map(new JSONObject(string));
                this.loadedMaps.add(map);
                Bukkit.getConsoleSender().sendMessage("§aSuccessfully loaded map " + map.getName());
            });

        if(this.loadedMaps.isEmpty())
            Bukkit.getConsoleSender().sendMessage("§cNo maps found");
        else
            this.pickRandomMap();
    }

    public void saveData() {
        if(this.lobbyLocation != null)
            this.configuration.set("lobby", this.getJSONObjectFromLocation(this.lobbyLocation).toString());

        if(!this.loadedMaps.isEmpty())
            this.configuration.set("maps", this.getMapList());

        try {
            this.configuration.save(this.locationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pickRandomMap() {
        this.setMap(this.loadedMaps.get(new Random().nextInt(this.loadedMaps.size())));
    }

    public void setMap(String name) {
        final Map map = this.loadedMaps.stream().filter(maps -> maps.getName().equals(name)).findAny().orElse(null);
        if(map != null)
            this.setMap(map);
    }

    private void setMap(Map map) {
        this.selectedMap = map;
        Bukkit.broadcastMessage(JumpRace.getInstance().getPrefix() + "§7The map §b" + this.selectedMap.getName() + " §7has been selected");
        Bukkit.getOnlinePlayers().forEach(players -> new PlayerScoreboard().updateScoreboard(players, null));
    }

    public String getCurrentMapName() {
        return ((this.selectedMap == null) ? "§c-" : this.selectedMap.getName());
    }

    private List<String> getMapList() {
        List<String> list = Lists.newCopyOnWriteArrayList();
        this.loadedMaps.forEach(map -> list.add(map.toJSONObject().toString()));
        return list;
    }

    private Location getLocationFromJSONObject(JSONObject location) {
        return new Location(Bukkit.getWorld(location.getString("world")), location.getDouble("x"),
                location.getDouble("y"), location.getDouble("z"), location.getFloat("yaw"), location.getFloat("pitch"));
    }

    private JSONObject getJSONObjectFromLocation(Location location) {
        return new JSONObject().put("world", location.getWorld().getName())
                .put("x", location.getX()).put("y", location.getY()).put("z", location.getZ())
                .put("yaw", location.getYaw()).put("pitch", location.getPitch());
    }

}
