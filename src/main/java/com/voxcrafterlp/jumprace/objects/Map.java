package com.voxcrafterlp.jumprace.objects;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
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
    private final List<Location> spawnLocations, endPointLocations;
    private Location endPointLocation;

    public Map(JSONObject jsonObject) {
        this.name = jsonObject.getString("name");
        this.spawnLocations = Lists.newCopyOnWriteArrayList();
        this.endPointLocations = Lists.newCopyOnWriteArrayList();
        this.loadWorld();
        jsonObject.getJSONArray("locations").forEach(location -> this.spawnLocations.add(this.getLocationFromJSONObject((JSONObject) location)));
        jsonObject.getJSONArray("endpoints").forEach(location -> this.endPointLocations.add(this.getLocationFromJSONObject((JSONObject) location)));
    }

    public Map(String name, List<Location> spawnLocations, List<Location> endPointLocations) {
        this.name = name;
        this.spawnLocations = spawnLocations;
        this.endPointLocations = endPointLocations;
    }

    /**
     * Select a random spawn location which isn't near a player's current position
     * @return Random location
     */
    public Location getRandomSpawnLocation() {
        if(this.spawnLocations.isEmpty()) return null;

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

    /**
     * Spawn the end point on a randomly selected location
     */
    public void spawnEndPoint() {
        if(this.endPointLocations.isEmpty()) return;

        this.endPointLocation = this.endPointLocations.get(new Random().nextInt(this.endPointLocations.size()));
        this.endPointLocation.getBlock().setType(Material.BEACON);
        this.setBeaconBase(this.endPointLocation.clone().add(1.0, -1.0, 1.0)); //Offset to the first iron block
        Bukkit.getConsoleSender().sendMessage("§aA beacon has been spawned at: X:" + this.endPointLocation.getBlockX() + " Y:" + this.endPointLocation.getBlockY() +
                " Z:" + this.endPointLocation.getBlockZ());
    }

    /**
     * Set the iron blocks required for the beacon to light up
     * @param firstBlock Location of the the first iron block
     */
    private void setBeaconBase(Location firstBlock) {
        for(int x = firstBlock.getBlockX(); x>(firstBlock.getBlockX() - 3); x--) {
            for(int z = firstBlock.getBlockZ(); z>(firstBlock.getBlockZ() - 3); z--)
                firstBlock.getWorld().getBlockAt(x, firstBlock.getBlockY(), z).setType(Material.IRON_BLOCK);
        }
    }

    /**
     * Convert a {@link Map} into a {@link JSONObject}
     * @return JSONObject containing the map information
     */
    public JSONObject toJSONObject() {
        return new JSONObject().put("name", this.name)
                .put("locations", new JSONArray().putAll(this.getLocationsAsJSONObjects(this.spawnLocations)))
                .put("endpoints", new JSONArray().putAll(this.getLocationsAsJSONObjects(this.endPointLocations)));
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
     * Put spawn locations into a list
     * @param locations List containing the locations which should be converted to JSONObjects
     * @return List containing every spawn location as a {@link JSONObject}
     */
    private List<JSONObject> getLocationsAsJSONObjects(List<Location> locations) {
        List<JSONObject> list = Lists.newCopyOnWriteArrayList();
        locations.forEach(location -> list.add(new JSONObject().put("world", location.getWorld().getName())
                .put("x", location.getX()).put("y", location.getY()).put("z", location.getZ())
                .put("yaw", location.getYaw()).put("pitch", location.getPitch())));
        return list;
    }

    private void loadWorld() {
        Bukkit.createWorld(new WorldCreator(this.name));
    }

}
