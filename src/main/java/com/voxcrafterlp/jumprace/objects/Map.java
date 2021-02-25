package com.voxcrafterlp.jumprace.objects;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.02.2021
 * Time: 16:06
 * Project: JumpRace
 */

@Getter
public class Map {

    private final String name;
    private final List<Location> spawnLocations;

    public Map(JSONObject jsonObject) {
        this.name = jsonObject.getString("name");
        this.spawnLocations = Lists.newCopyOnWriteArrayList();
        jsonObject.getJSONArray("locations").forEach(location -> this.spawnLocations.add(this.getLocationFromJSONObject((JSONObject) location)));
    }

    public Map(String name, List<Location> spawnLocations) {
        this.name = name;
        this.spawnLocations = spawnLocations;
    }

    public Location getRandomSpawnLocation() {
        if(spawnLocations.isEmpty()) return null;

        while (true) {
            final Location location = this.spawnLocations.get(new Random().nextInt(this.spawnLocations.size()));
            AtomicBoolean isLocationSafe = new AtomicBoolean(true);

            Bukkit.getOnlinePlayers().forEach(player -> {
                if(player.getLocation().distance(location) < 10)
                    isLocationSafe.set(false);
            });

            if(isLocationSafe.get())
                return location;
        }
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("name", this.name).put("locations", new JSONArray().putAll(this.getSpawnLocationsAsJSONObjects()));
    }

    private Location getLocationFromJSONObject(JSONObject location) {
        return new Location(Bukkit.getWorld(location.getString("world")), location.getDouble("x"),
                location.getDouble("y"), location.getDouble("z"), location.getFloat("yaw"), location.getFloat("pitch"));
    }

    private List<JSONObject> getSpawnLocationsAsJSONObjects() {
        List<JSONObject> list = Lists.newCopyOnWriteArrayList();
        this.spawnLocations.forEach(location -> list.add(new JSONObject().put("world", location.getWorld().getName())
                .put("x", location.getX()).put("y", location.getY()).put("z", location.getZ())
                .put("yaw", location.getYaw()).put("pitch", location.getPitch())));
        return list;
    }

}
