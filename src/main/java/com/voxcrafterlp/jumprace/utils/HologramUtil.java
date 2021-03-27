package com.voxcrafterlp.jumprace.utils;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 14.03.2021
 * Time: 12:55
 * Project: JumpRace
 */

public class HologramUtil {

    /**
     * Summon an {@link EntityArmorStand} for a player
     * Send a {@link PacketPlayOutSpawnEntityLiving} to the player
     * @param player Player who the packet should be sent to
     * @param location Location of the armorstand
     * @param text Display name of the armorstand
     */
    public void summonArmorStand(Player player, Location location, String text) {
        final EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle());
        armorStand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);
        armorStand.setInvisible(true);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(armorStand));
    }

    /**
     * Summon an {@link EntityArmorStand} for a player
     * Send a {@link PacketPlayOutSpawnEntityLiving} to the player
     * @param player Player who the packet should be sent to
     * @param entityArmorStand Armorstand which should be sent to the player
     * @return Entity ID of the armorstand
     */
    public int summonArmorStand(Player player, EntityArmorStand entityArmorStand) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStand));
        return entityArmorStand.getId();
    }

    /**
     * Remove an armorstand for a player
     * Send a {@link PacketPlayOutEntityDestroy} to the player
     * @param player Player who should receive the packet
     * @param entityID Entity ID of the armorstand
     */
    public void removeArmorStand(Player player, int entityID) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityID));
    }

}
