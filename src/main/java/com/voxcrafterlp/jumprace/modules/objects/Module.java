package com.voxcrafterlp.jumprace.modules.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.enums.ParticleDirection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:15
 * Project: JumpRace
 */

@Getter @Setter
public class Module {

    private String name;
    private String builder;
    private ModuleDifficulty moduleDifficulty;
    private ModuleData moduleData;

    private RelativePosition startPoint, endPoint;
    private RelativePosition border1, border2;

    private Location spawnLocation, startPointLocation, endPointLocation;

    private int particlesTaskID;
    private List<ParticleLine> particleLines;

    public Module(String name, String builder, ModuleDifficulty moduleDifficulty, ModuleData moduleData, RelativePosition startPoint, RelativePosition endPoint, boolean loadDefaults) {
        this.name = name;
        this.builder = builder;
        this.moduleDifficulty = moduleDifficulty;
        this.moduleData = moduleData;
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        if(loadDefaults) {
            this.moduleData = JumpRace.getInstance().getModuleLoader().getDefaultModule().getModuleData();
            this.startPoint = JumpRace.getInstance().getModuleLoader().getDefaultModule().getStartPoint();
            this.endPoint = JumpRace.getInstance().getModuleLoader().getDefaultModule().getEndPoint();
        }

        this.border1 = new RelativePosition(0, 0, 0);
        this.border2 = new RelativePosition((this.moduleData.getWidth() - 1), (this.moduleData.getHeight() - 1), (this.moduleData.getLength() - 1));
    }

    public void build(Location location, boolean isLastModule) {
        this.spawnLocation = location;

        for(int x = 0; x < moduleData.getWidth(); x++) {
            for(int y = 0; y < moduleData.getHeight(); y++) {
                for(int z = 0; z < moduleData.getLength(); z++) {
                    int index = y * moduleData.getWidth() * moduleData.getLength() + z * moduleData.getWidth() + x;
                    int b = moduleData.getBlocks()[index] & 0xFF; //make the block unsigned
                    Material material = Material.getMaterial(b);
                    if(material != Material.AIR) {
                        Block block = new Location(location.getWorld(), location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z).getBlock();
                        block.setType(material, true); //TODO try false
                        block.setData(moduleData.getData()[index]);
                    }
                }
            }
        }

        this.startPointLocation = calculateLocation(location, startPoint);
        this.endPointLocation = calculateLocation(location, endPoint);

        location.getWorld().getBlockAt(this.startPointLocation).setType(Material.GOLD_BLOCK);
        location.getWorld().getBlockAt(this.endPointLocation).setType(isLastModule ? Material.EMERALD_BLOCK : Material.GOLD_BLOCK);

        if(JumpRace.getInstance().getJumpRaceConfig().isBuilderServer()) {
            this.recalculateParticles();
            this.spawnParticles();
        }
    }

    public void spawnParticles() {
        this.particlesTaskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () -> {
            this.particleLines.forEach(ParticleLine::draw);
        }, 20, 4);
    }

    /**
     * Calculates the border particles based on the size of the module
     */
    public void recalculateParticles() {
        int width = this.border2.getRelativeZ() - this.border1.getRelativeZ() + 1;
        int depth = this.border2.getRelativeX() - this.border1.getRelativeX() + 1;
        int height = this.border2.getRelativeY() - this.border1.getRelativeY() + 1;

        RelativePosition border3, border4, border5;
        border3 = new RelativePosition(border1.getRelativeX(), border1.getRelativeY(), (border1.getRelativeZ() + width));
        border4 = new RelativePosition((border1.getRelativeX() + depth), border1.getRelativeY(), border1.getRelativeZ());
        border5 = new RelativePosition(border1.getRelativeX(), (border1.getRelativeY() + height), border1.getRelativeZ());

        List<ParticleLine> particleLines = Lists.newCopyOnWriteArrayList();

        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, this.border1), ParticleDirection.EAST, depth));
        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, this.border1), ParticleDirection.UP, height));
        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, this.border1), ParticleDirection.SOUTH, width));

        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, new RelativePosition(this.border2).getForParticles()), ParticleDirection.WEST, depth));
        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, new RelativePosition(this.border2).getForParticles()), ParticleDirection.DOWN, height));
        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, new RelativePosition(this.border2).getForParticles()), ParticleDirection.NORTH, width));

        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, border3), ParticleDirection.UP, height));
        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, border3), ParticleDirection.EAST, depth));

        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, border4), ParticleDirection.UP, height));
        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, border4), ParticleDirection.SOUTH, width));

        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, border5), ParticleDirection.EAST, depth));
        particleLines.add(new ParticleLine(this.calculateLocation(this.spawnLocation, border5), ParticleDirection.SOUTH, width));

        this.particleLines = particleLines;
        this.startPointLocation = calculateLocation(this.spawnLocation, startPoint);
        this.endPointLocation = calculateLocation(this.spawnLocation, endPoint);
    }

    public void stopParticles() {
        Bukkit.getScheduler().cancelTask(this.particlesTaskID);
    }

    public Location calculateLocation(Location location, RelativePosition relativePosition) {
        return new Location(location.getWorld(), (location.getX() + relativePosition.getRelativeX()), (location.getY() + relativePosition.getRelativeY()), (location.getZ()) + relativePosition.getRelativeZ());
    }

    public Location[] getModuleBorders() {
        return new Location[]{this.calculateLocation(this.spawnLocation, this.border1), this.calculateLocation(this.spawnLocation, this.border2)};
    }

    public void saveModule() {
        new File("plugins/JumpRace/modules/" + this.name).mkdir();

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
        final Location[] moduleBorders = this.getModuleBorders();
        moduleBorders[1] = moduleBorders[1].add(1.0, 1.0, 1.0);

        final int width = moduleBorders[1].getBlockX() - moduleBorders[0].getBlockX();
        final int height = moduleBorders[1].getBlockY() - moduleBorders[0].getBlockY();
        final int length = moduleBorders[1].getBlockZ() - moduleBorders[0].getBlockZ();

        final TreeMap<Integer, Byte> blockMap = new TreeMap<>();
        final TreeMap<Integer, Byte> dataMap = new TreeMap<>();

        for(int x = moduleBorders[0].getBlockX(); x<moduleBorders[1].getBlockX(); x++) {
            for(int y = moduleBorders[0].getBlockY(); y<moduleBorders[1].getBlockY(); y++) {
                for(int z = moduleBorders[0].getBlockZ(); z<moduleBorders[1].getBlockZ(); z++) {
                    int index = x + (y * length + z) * width;
                    final Block block = spawnLocation.getWorld().getBlockAt(new Location(spawnLocation.getWorld(), x, y, z));
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
