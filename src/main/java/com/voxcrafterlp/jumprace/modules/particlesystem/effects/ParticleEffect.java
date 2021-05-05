package com.voxcrafterlp.jumprace.modules.particlesystem.effects;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import com.voxcrafterlp.jumprace.modules.particlesystem.Action;
import com.voxcrafterlp.jumprace.modules.particlesystem.ParticleEffectData;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.EffectType;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ParticleType;
import com.voxcrafterlp.jumprace.modules.utils.CalculatorUtil;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 24.04.2021
 * Time: 16:20
 * Project: JumpRace
 */

@Getter @Setter
public abstract class ParticleEffect {

    private Location location;
    private RelativePosition relativePosition;
    private ParticleType particleType;
    private int yaw, pitch, roll;
    private double size;
    private final List<Player> visibleTo;
    private final Action action;

    private int taskID;
    private Inventory effectInventory;

    public ParticleEffect(RelativePosition relativePosition, ParticleType particleType, int yaw, int pitch, int roll, double size, List<Player> visibleTo, Location moduleLocation, Action action) {
        this.relativePosition = relativePosition;
        this.particleType = particleType;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.size = size;
        this.visibleTo = visibleTo;
        this.action = action;

        this.location = new CalculatorUtil().calculateLocation(moduleLocation, this.relativePosition);
    }

    /**
     * Starts a scheduler which calls the {@link ParticleEffect#draw()} method every 4 ticks
     */
    public void startDrawing() {
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(JumpRace.getInstance(), this::draw, 10, 4);
    }

    /**
     * Stops the scheduler that is drawing the particle effect
     */
    public void stopDrawing() {
        Bukkit.getScheduler().cancelTask(this.taskID);
    }

    public abstract void draw();

    /**
     * Sends a {@link PacketPlayOutWorldParticles} packet to players in a 50 block radius
     * @param packet Packet that should be send
     * @param location Location of the particle effect
     */
    public void sendPacket(PacketPlayOutWorldParticles packet, Location location) {
        this.visibleTo.forEach(player -> {
            if(location.distance(player.getLocation()) <= 50)
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        });
    }

    public void buildInventory() {
        this.effectInventory = Bukkit.createInventory(null, 54, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-effectsettings-inventory"));
        final ItemStack background = new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build();

        for(int i = 0; i<this.effectInventory.getSize(); i++)
            this.effectInventory.setItem(i, background);

        this.effectInventory.setItem(10, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§a+10").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA0MGZlODM2YTZjMmZiZDJjN2E5YzhlYzZiZTUxNzRmZGRmMWFjMjBmNTVlMzY2MTU2ZmE1ZjcxMmUxMCJ9fX0="));
        this.effectInventory.setItem(12, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§a+10").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA0MGZlODM2YTZjMmZiZDJjN2E5YzhlYzZiZTUxNzRmZGRmMWFjMjBmNTVlMzY2MTU2ZmE1ZjcxMmUxMCJ9fX0="));
        this.effectInventory.setItem(14, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§a+0.1").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA0MGZlODM2YTZjMmZiZDJjN2E5YzhlYzZiZTUxNzRmZGRmMWFjMjBmNTVlMzY2MTU2ZmE1ZjcxMmUxMCJ9fX0="));
        this.effectInventory.setItem(28, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§c-10").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQzNzM0NmQ4YmRhNzhkNTI1ZDE5ZjU0MGE5NWU0ZTc5ZGFlZGE3OTVjYmM1YTEzMjU2MjM2MzEyY2YifX19"));
        this.effectInventory.setItem(30, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§c-10").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQzNzM0NmQ4YmRhNzhkNTI1ZDE5ZjU0MGE5NWU0ZTc5ZGFlZGE3OTVjYmM1YTEzMjU2MjM2MzEyY2YifX19"));
        this.effectInventory.setItem(32, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§c-0.1").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQzNzM0NmQ4YmRhNzhkNTI1ZDE5ZjU0MGE5NWU0ZTc5ZGFlZGE3OTVjYmM1YTEzMjU2MjM2MzEyY2YifX19"));

        this.effectInventory.setItem(25, new ItemManager(Material.ENDER_PEARL).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-effectsettings-teleport-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-effectsettings-teleport-description")).build());
        this.effectInventory.setItem(34, new ItemManager(Material.BARRIER).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-effectsettings-delete-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-effectsettings-delete-description")).build());

        this.effectInventory.setItem(45, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-addeffect-back-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-addeffect-back-description")).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="));
        this.effectInventory.setItem(49, new ItemManager(Material.REDSTONE_COMPARATOR).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-effectsettings-configure-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-effectsettings-configure-description")).build());

        this.updateInventory();
    }

    public void updateInventory() {
        this.effectInventory.setItem(19, new ItemManager(Material.BOOK).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-effectsettings-yaw-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-effectsettings-yaw-description", String.valueOf(this.yaw))).build());
        this.effectInventory.setItem(21, new ItemManager(Material.BOOK).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-effectsettings-pitch-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-effectsettings-pitch-description", String.valueOf(this.pitch))).build());
        this.effectInventory.setItem(23, new ItemManager(Material.BOOK).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-effectsettings-size-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-effectsettings-size-description", String.valueOf(this.size))).build());

        this.effectInventory.setItem(16, new ItemManager(Material.BLAZE_POWDER).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-effectsettings-particles-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-effectsettings-particles-description", this.particleType.getDisplayName())).build());
    }

    public abstract EffectType getEffectType();

    public abstract ParticleEffectData getEffectData();

}
