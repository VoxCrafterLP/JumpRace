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

    /**
     * Create the 'locations.yml' file & load locations and maps
     */
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

    /**
     * Load the lobby spawn location & maps with their locations
     */
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

    /**
     * Save spawn locations
     */
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

    /**
     * Pick random map from the {@link #loadedMaps} {@link List}
     */
    public void pickRandomMap() {
        this.setMap(this.loadedMaps.get(new Random().nextInt(this.loadedMaps.size())));
    }

    /**
     * Get map by name and select it
     * @param name Name of the map
     */
    public void setMap(String name) {
        final Map map = this.loadedMaps.stream().filter(maps -> maps.getName().equals(name)).findAny().orElse(null);
        if(map != null)
            this.setMap(map);
    }

    private void setMap(Map map) {
        this.selectedMap = map;
        Bukkit.broadcastMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-map-selected", this.selectedMap.getName()));
        Bukkit.getOnlinePlayers().forEach(players -> new PlayerScoreboard().updateScoreboard(players, null));
    }

    public String getCurrentMapName() {
        return ((this.selectedMap == null) ? "§c-" : this.selectedMap.getName());
    }

    /**
     * Get loaded maps & put them into a list as json strings
     * @return List containing every map as a json string
     */
    private List<String> getMapList() {
        List<String> list = Lists.newCopyOnWriteArrayList();
        this.loadedMaps.forEach(map -> list.add(map.toJSONObject().toString()));
        return list;
    }

    /**
     * Parse {@link Location} from a {@link JSONObject}
     * @param jsonObject {@link JSONObject} containing a bukkit location
     * @return Location parsed from the {@link JSONObject}
     */
    private Location getLocationFromJSONObject(JSONObject jsonObject) {
        return new Location(Bukkit.getWorld(jsonObject.getString("world")), jsonObject.getDouble("x"),
                jsonObject.getDouble("y"), jsonObject.getDouble("z"), jsonObject.getFloat("yaw"), jsonObject.getFloat("pitch"));
    }

    /**
     * Convert a {@link Location} to a {@link JSONObject}
     * @param location Location which should be converted into a {@link JSONObject}
     * @return JSONObject containing a bukkit location
     */
    private JSONObject getJSONObjectFromLocation(Location location) {
        return new JSONObject().put("world", location.getWorld().getName())
                .put("x", location.getX()).put("y", location.getY()).put("z", location.getZ())
                .put("yaw", location.getYaw()).put("pitch", location.getPitch());
    }

}
