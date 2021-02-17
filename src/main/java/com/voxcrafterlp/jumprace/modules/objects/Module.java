package com.voxcrafterlp.jumprace.modules.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.enums.ParticleDirection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        this.recalculateParticles();
        this.spawnParticles();
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

    }

    private void saveSchematic() {
        final Location[] moduleBorders = this.getModuleBorders();

        final int width = moduleBorders[1].getBlockX() - moduleBorders[0].getBlockX();
        final int height = moduleBorders[1].getBlockY() - moduleBorders[0].getBlockY();
        final int length = moduleBorders[1].getBlockZ() - moduleBorders[0].getBlockZ();

        final Array blocks = (Array) Array.newInstance(Byte.class);
        final Array data = (Array) Array.newInstance(Byte.class);

        for(int x = moduleBorders[0].getBlockX(); x<moduleBorders[1].getBlockX(); x++) {
            for(int y = moduleBorders[0].getBlockY(); y<moduleBorders[1].getBlockY(); y++) {
                for(int z = moduleBorders[0].getBlockZ(); z<moduleBorders[1].getBlockZ(); z++) {
                    int index = x + (y * length + z) * width;
                    final Block block = spawnLocation.getWorld().getBlockAt(new Location(spawnLocation.getWorld(), x, y, z));
                    Array.setByte(blocks, index, (byte) block.getTypeId());
                    Array.setByte(data, index, block.getData());
                }
            }
        }

        //TODO nbt

    }

}
