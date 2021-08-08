package com.voxcrafterlp.jumprace.modules.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.enums.ParticleDirection;
import com.voxcrafterlp.jumprace.modules.particlesystem.ParticleManager;
import com.voxcrafterlp.jumprace.modules.particlesystem.ParticleUI;
import com.voxcrafterlp.jumprace.modules.utils.CalculatorUtil;
import com.voxcrafterlp.jumprace.modules.utils.ModuleExportUtil;
import com.voxcrafterlp.jumprace.utils.HologramUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

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
    private ParticleManager particleManager;
    private ParticleUI particleUI;

    private RelativePosition startPoint, endPoint;
    private RelativePosition border1, border2;

    private Location spawnLocation, startPointLocation, endPointLocation, goldPlateLocation;

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

    /**
     * Builds the module
     * @param location Lower left corner of the module
     * @param isLastModule Determines the material of the last block
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
                        final Block block = new Location(location.getWorld(), location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z).getBlock();
                        block.setType(material, false);
                        block.setData(moduleData.getData()[index]);
                    }
                }
            }
        }

        for(int i = 0; i<this.moduleData.getTileEntities().size(); i++) {
            final NBTTagCompound nbtTileEntity = this.moduleData.getTileEntities().get(i);
            final TileEntity tileEntity = ((CraftWorld) this.spawnLocation.getWorld())
                    .getTileEntityAt(nbtTileEntity.getInt("x"), nbtTileEntity.getInt("y"), nbtTileEntity.getInt("z"));

            if(tileEntity != null) {
                tileEntity.a(nbtTileEntity);
                tileEntity.update();
            }
        }

        this.startPointLocation = new CalculatorUtil().calculateLocation(location, startPoint);
        this.endPointLocation = new CalculatorUtil().calculateLocation(location, endPoint);

        location.getWorld().getBlockAt(this.startPointLocation).setType(Material.GOLD_BLOCK);
        location.getWorld().getBlockAt(this.endPointLocation).setType(isLastModule ? Material.EMERALD_BLOCK : Material.GOLD_BLOCK);

        if(JumpRace.getInstance().getJumpRaceConfig().isBuilderServer()) {
            this.recalculateParticles();
            this.spawnParticles();
        } else  {
            this.goldPlateLocation =  this.endPointLocation.clone().add(0.0, 1.0, 0.0);
            this.goldPlateLocation.getBlock().setType(Material.GOLD_PLATE);
        }
    }

    /**
     * Starts the particle scheduler
     */
    public void spawnParticles() {
        this.particlesTaskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), () ->
                this.particleLines.forEach(ParticleLine::draw), 20, 4);
    }

    /**
     * Calculates the border particles based on the size of the module
     */
    public void recalculateParticles() {
        int width = (int) this.border2.getRelativeZ() - (int) this.border1.getRelativeZ() + 1;
        int depth = (int) this.border2.getRelativeX() - (int) this.border1.getRelativeX() + 1;
        int height = (int) this.border2.getRelativeY() - (int) this.border1.getRelativeY() + 1;

        RelativePosition border3, border4, border5;
        border3 = new RelativePosition(border1.getRelativeX(), border1.getRelativeY(), (border1.getRelativeZ() + width));
        border4 = new RelativePosition((border1.getRelativeX() + depth), border1.getRelativeY(), border1.getRelativeZ());
        border5 = new RelativePosition(border1.getRelativeX(), (border1.getRelativeY() + height), border1.getRelativeZ());

        final List<ParticleLine> particleLines = Lists.newCopyOnWriteArrayList();

        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, this.border1), ParticleDirection.EAST, depth));
        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, this.border1), ParticleDirection.UP, height));
        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, this.border1), ParticleDirection.SOUTH, width));

        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, new RelativePosition(this.border2).getForParticles()), ParticleDirection.WEST, depth));
        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, new RelativePosition(this.border2).getForParticles()), ParticleDirection.DOWN, height));
        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, new RelativePosition(this.border2).getForParticles()), ParticleDirection.NORTH, width));

        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, border3), ParticleDirection.UP, height));
        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, border3), ParticleDirection.EAST, depth));

        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, border4), ParticleDirection.UP, height));
        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, border4), ParticleDirection.SOUTH, width));

        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, border5), ParticleDirection.EAST, depth));
        particleLines.add(new ParticleLine(new CalculatorUtil().calculateLocation(this.spawnLocation, border5), ParticleDirection.SOUTH, width));

        this.particleLines = particleLines;
        this.startPointLocation = new CalculatorUtil().calculateLocation(this.spawnLocation, startPoint);
        this.endPointLocation = new CalculatorUtil().calculateLocation(this.spawnLocation, endPoint);
    }

    /**
     * Stops the particle scheduler
     */
    public void stopParticles() {
        Bukkit.getScheduler().cancelTask(this.particlesTaskID);
    }

    /**
     * Gets the module's borders
     * @return Array containing the lower left and the upper right corner
     */
    public Location[] getModuleBorders() {
        return new Location[]{new CalculatorUtil().calculateLocation(this.spawnLocation, this.border1), new CalculatorUtil().calculateLocation(this.spawnLocation, this.border2)};
    }

    /**
     * Save the module
     * @param borders Borders of the module
     * @param startPoint RelativePosition of the start block
     * @param endPoint RelativePosition of the end block
     */
    public void saveModule(Location[] borders, RelativePosition startPoint, RelativePosition endPoint) {
        new File("plugins/JumpRace/modules/" + this.name).mkdir();
        new ModuleExportUtil(this.name, this.builder, this.moduleDifficulty, startPoint, endPoint, borders, this.spawnLocation, this.particleManager.getParticleEffects()).exportModule();
    }

    /**
     * Spawn the module hologram
     * @param player Player who the hologram should be sent to
     * @param moduleNumber Number of the module in the {@link ModuleRow}
     */
    public void spawnHologram(Player player, int moduleNumber) {
        final List<String> lines = Lists.newCopyOnWriteArrayList();

        lines.add(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-hologram-line-1"));
        lines.add(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-hologram-line-2", String.valueOf(moduleNumber)));
        lines.add(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-hologram-line-3", this.name));
        lines.add(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-hologram-line-4", this.builder));
        lines.add(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-hologram-line-5", this.moduleDifficulty.getDisplayName()));
        lines.add(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("module-hologram-line-6"));

        final Location hologramLocation = this.startPointLocation.clone().add(2.0, 0.7 + (0.3 * lines.size()), 0.5); //Hologram offset
        lines.forEach(line -> new HologramUtil().summonArmorStand(player, hologramLocation.add(0.0, -0.3, 0.0), line));
    }

    /**
     * Calculate the player's spawn location
     * @return Spawn location
     */
    public Location getPlayerSpawnLocation() {
        final Location spawnLocation = new CalculatorUtil().calculateLocation(this.getSpawnLocation(), this.getStartPoint());
        spawnLocation.setX(spawnLocation.getX() + 0.5);
        spawnLocation.setY(spawnLocation.getY() + 1.0);
        spawnLocation.setZ(spawnLocation.getZ() + 0.5);
        spawnLocation.setYaw(-90F);
        spawnLocation.setPitch(1.0F);

        return spawnLocation;
    }

    /**
     * Initializes the {@link ParticleManager} and the {@link ParticleUI} if the server is configured as a builder server
     * @param player Player required to initialize the {@link ParticleManager}
     */
    public void initializeParticleManager(Player player) {
        this.particleManager = new ParticleManager(this, player);
        if(JumpRace.getInstance().getJumpRaceConfig().isBuilderServer())
            this.particleUI = new ParticleUI(this, player);
    }

    /**
     * Disables the particle system with all module related schedulers
     */
    public void disableParticleSystem() {
        if(this.particleManager != null)
            this.particleManager.stopEffects();
    }

    @Override
    public Module clone() {
        final Module module = new Module(this.name, this.builder, this.moduleDifficulty, this.moduleData, this.startPoint, this.endPoint, false);
        module.setSpawnLocation(this.spawnLocation);
        module.setStartPointLocation(this.startPointLocation);
        module.setEndPointLocation(this.endPointLocation);

        return module;
    }

}
