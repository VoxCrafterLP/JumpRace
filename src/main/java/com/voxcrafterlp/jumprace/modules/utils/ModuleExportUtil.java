package com.voxcrafterlp.jumprace.modules.utils;

import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.02.2021
 * Time: 14:14
 * Project: JumpRace
 */

public class ModuleExportUtil {

    private final String name;
    private final String builder;
    private final ModuleDifficulty moduleDifficulty;
    private final RelativePosition startPoint;
    private final RelativePosition endPoint;
    private final Location[] moduleBorders;
    private final Location spawnLocation;

    public ModuleExportUtil(String name, String builder, ModuleDifficulty moduleDifficulty, RelativePosition startPoint, RelativePosition endPoint, Location[] moduleBorders, Location spawnLocation) {
        this.name = name;
        this.builder = builder;
        this.moduleDifficulty = moduleDifficulty;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.moduleBorders = moduleBorders;
        this.spawnLocation = spawnLocation;
    }

    public void exportModule() {
        try {
            this.saveProperties();
            this.saveSchematic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProperties() throws IOException {
        final File propertiesFile = new File("plugins/JumpRace/modules/" + this.name + "/module.json");
        final JSONObject properties = new JSONObject();

        properties.put("name", this.name).put("builder", this.builder).put("difficulty", this.moduleDifficulty.getConfigName());
        properties.put("startpoint", this.startPoint.toJSONObject()).put("endpoint", this.endPoint.toJSONObject());

        if(!propertiesFile.exists())
            propertiesFile.createNewFile();

        FileWriter myWriter = new FileWriter("plugins/JumpRace/modules/" + this.name + "/module.json");
        myWriter.write(properties.toString());
        myWriter.close();
    }

    private void saveSchematic() throws IOException {
        final int width = this.moduleBorders[1].getBlockX() - this.moduleBorders[0].getBlockX();
        final int height = this.moduleBorders[1].getBlockY() - this.moduleBorders[0].getBlockY();
        final int length = this.moduleBorders[1].getBlockZ() - this.moduleBorders[0].getBlockZ();

        final TreeMap<Integer, Byte> blockMap = new TreeMap<>();
        final TreeMap<Integer, Byte> dataMap = new TreeMap<>();

        for(int x = this.moduleBorders[0].getBlockX(); x<this.moduleBorders[1].getBlockX(); x++) {
            for(int y = this.moduleBorders[0].getBlockY(); y<this.moduleBorders[1].getBlockY(); y++) {
                for(int z = this.moduleBorders[0].getBlockZ(); z<this.moduleBorders[1].getBlockZ(); z++) {
                    int index = x + (y * length + z) * width;
                    final Block block = this.spawnLocation.getWorld().getBlockAt(new Location(this.spawnLocation.getWorld(), x, y, z));
                    blockMap.put(index, (byte) block.getTypeId());
                    dataMap.put(index, block.getData());
                }
            }
        }

        byte[] blocks = this.convertMapToByteArray(blockMap);
        byte[] data = this.convertMapToByteArray(dataMap);

        final File schematicFile = new File("plugins/JumpRace/modules/" + this.name + "/module.schematic");

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInt("Width", width);
        nbt.setInt("Height", height);
        nbt.setInt("Length", length);
        nbt.setString("Materials", "Alpha");
        nbt.setByteArray("Blocks", blocks);
        nbt.setByteArray("Data", data);

        NBTCompressedStreamTools.a(nbt, new FileOutputStream(schematicFile));
    }

    private byte[] convertMapToByteArray(TreeMap<Integer, Byte> map) {
        Byte[] array =  map.values().toArray(new Byte[0]);
        byte[] bytes = new byte[array.length];

        int i = 0;
        for(Byte b: array)
            bytes[i++] = b;

        return bytes;
    }

}
