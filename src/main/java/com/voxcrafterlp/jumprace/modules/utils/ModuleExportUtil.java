package com.voxcrafterlp.jumprace.modules.utils;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import com.voxcrafterlp.jumprace.modules.particlesystem.effects.ParticleEffect;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
    private final List<ParticleEffect> particleEffects;

    public ModuleExportUtil(String name, String builder, ModuleDifficulty moduleDifficulty, RelativePosition startPoint, RelativePosition endPoint, Location[] moduleBorders, Location spawnLocation, List<ParticleEffect> particleEffects) {
        this.name = name;
        this.builder = builder;
        this.moduleDifficulty = moduleDifficulty;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.moduleBorders = moduleBorders;
        this.spawnLocation = spawnLocation;
        this.particleEffects = particleEffects;
    }

    /**
     * Save the module's schematic and properties file.
     * Reload modules
     */
    public void exportModule() {
        try {
            this.saveProperties();
            this.saveSchematic();
        } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

        //Reload modules
        Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> JumpRace.getInstance().getModuleLoader().reloadModules(), 3);
    }

    /**
     * Save the properties file of the module
     * @throws IOException If an I/O error occurred
     */
    private void saveProperties() throws IOException {
        final File propertiesFile = new File("plugins/JumpRace/modules/" + this.name + "/module.json");
        final JSONObject properties = new JSONObject();

        properties.put("name", this.name).put("builder", this.builder).put("difficulty", this.moduleDifficulty.getConfigName());
        properties.put("startpoint", this.startPoint.toJSONObject()).put("endpoint", this.endPoint.toJSONObject());

        final JSONArray particles = new JSONArray();
        this.particleEffects.forEach(particleEffect -> particles.put(particleEffect.getEffectData().toJSON()));
        properties.put("particles", particles);

        if(!propertiesFile.exists())
            propertiesFile.createNewFile();

        FileWriter myWriter = new FileWriter("plugins/JumpRace/modules/" + this.name + "/module.json");
        myWriter.write(properties.toString());
        myWriter.close();
    }

    /**
     * Generate and save the schematic file of the module
     * @throws IOException If an I/O error occurred
     */
    private void saveSchematic() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.moduleBorders[1] = this.moduleBorders[1].add(1.0, 1.0, 1.0);

        final int width = this.moduleBorders[1].getBlockX() - this.moduleBorders[0].getBlockX();
        final int height = this.moduleBorders[1].getBlockY() - this.moduleBorders[0].getBlockY();
        final int length = this.moduleBorders[1].getBlockZ() - this.moduleBorders[0].getBlockZ();

        final TreeMap<Integer, Byte> blockMap = new TreeMap<>();
        final TreeMap<Integer, Byte> dataMap = new TreeMap<>();
        final List<NBTTagCompound> tileEntities = Lists.newCopyOnWriteArrayList();

        for(int x = this.moduleBorders[0].getBlockX(); x<this.moduleBorders[1].getBlockX(); x++) {
            for(int y = this.moduleBorders[0].getBlockY(); y<this.moduleBorders[1].getBlockY(); y++) {
                for(int z = this.moduleBorders[0].getBlockZ(); z<this.moduleBorders[1].getBlockZ(); z++) {
                    int index = x + (y * length + z) * width;
                    final Block block = this.spawnLocation.getWorld().getBlockAt(new Location(this.spawnLocation.getWorld(), x, y, z));
                    blockMap.put(index, (byte) block.getTypeId());
                    dataMap.put(index, block.getData());

                    final TileEntity tileEntity = ((CraftWorld) block.getWorld())
                            .getTileEntityAt(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());

                    if(tileEntity != null) {
                        final NBTTagCompound tileEntityNBT = new NBTTagCompound();

                        if(tileEntity instanceof TileEntitySkull) {
                            ((TileEntitySkull) tileEntity).b(tileEntityNBT);
                        } else
                            tileEntity.b(tileEntityNBT);

                        tileEntities.add(tileEntityNBT);
                    }
                }
            }
        }

        byte[] blocks = this.convertMapToByteArray(blockMap);
        byte[] data = this.convertMapToByteArray(dataMap);

        final NBTTagList nbtTileEntities = new NBTTagList();
        tileEntities.forEach(nbtTileEntities::add);


        final File schematicFile = new File("plugins/JumpRace/modules/" + this.name + "/module.schematic");

        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInt("Width", width);
        nbt.setInt("Height", height);
        nbt.setInt("Length", length);
        nbt.setString("Materials", "Alpha");
        nbt.setByteArray("Blocks", blocks);
        nbt.setByteArray("Data", data);
        nbt.set("TileEntities", nbtTileEntities);

        NBTCompressedStreamTools.a(nbt, new FileOutputStream(schematicFile));
    }

    /**
     * Convert a {@link java.util.Map} to a byte array
     * @param map Map with the index as key and the byte value as value
     * @return Byte array containing the byte values from the map
     */
    private byte[] convertMapToByteArray(TreeMap<Integer, Byte> map) {
        Byte[] array = map.values().toArray(new Byte[0]);
        byte[] bytes = new byte[array.length];

        int i = 0;
        for(Byte b: array)
            bytes[i++] = b;

        return bytes;
    }

}
