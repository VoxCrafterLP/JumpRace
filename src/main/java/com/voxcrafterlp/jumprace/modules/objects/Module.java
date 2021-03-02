package com.voxcrafterlp.jumprace.modules.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.enums.ParticleDirection;
import com.voxcrafterlp.jumprace.modules.utils.ModuleExportUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    private Location spawnLocation, startPointLocation, endPointLocation, enderChestLocation;

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

    /*
     * https://www.spigotmc.org/threads/schematic-load-paste.302761/
     */
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
                        block.setType(material, false);
                        block.setData(moduleData.getData()[index]);

                        if(block.getType() == Material.ENDER_CHEST)
                            this.enderChestLocation = block.getLocation();
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

    public void saveModule(Location[] borders, RelativePosition startPoint, RelativePosition endPoint) {
        new File("plugins/JumpRace/modules/" + this.name).mkdir();
        new ModuleExportUtil(this.name, this.builder, this.moduleDifficulty, startPoint, endPoint, borders, this.spawnLocation).exportModule();
    }

    public void spawnHologram(Player player, int moduleNumber) {
        final List<String> lines = Lists.newCopyOnWriteArrayList();

        lines.addAll(Arrays.asList("§8===============", "§7Module §b#" + moduleNumber, "§7Name§8: §b" + this.name, "§7Builder(s)§8: §b" + this.builder,
                "§7Difficulty§8: " + this.moduleDifficulty.getDisplayName(), "§8==============="));

        double d = (0.3 * lines.size());
        System.out.println(d);

        final Location hologramLocation = this.startPointLocation.clone().add(2.0, 0.7 + d, 0.5); //Hologram offset
        lines.forEach(line -> this.summonArmorStand(player, hologramLocation.add(0.0, -0.3, 0.0), line));
    }

    private void summonArmorStand(Player player, Location location, String text) {
        final EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle());
        armorStand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);
        armorStand.setInvisible(true);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
    }

    public Location getPlayerSpawnLocation() {
        final Location spawnLocation = this.calculateLocation(this.getSpawnLocation(), this.getStartPoint());
        spawnLocation.setX(spawnLocation.getX() + 0.5);
        spawnLocation.setY(spawnLocation.getY() + 1.0);
        spawnLocation.setZ(spawnLocation.getZ() + 0.5);
        spawnLocation.setYaw(-90F);
        spawnLocation.setPitch(1.0F);

        return spawnLocation;
    }

    @Override
    public Module clone() {
        final Module module = new Module(this.name, this.builder, this.moduleDifficulty, this.moduleData, this.startPoint, this.endPoint, false);
        module.setSpawnLocation(this.spawnLocation);
        module.setStartPointLocation(this.startPointLocation);
        module.setEndPointLocation(this.endPointLocation);
        module.setEnderChestLocation(this.enderChestLocation);

        return module;
    }

}
