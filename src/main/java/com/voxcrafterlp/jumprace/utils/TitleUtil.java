package com.voxcrafterlp.jumprace.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.01.2021
 * Time: 00:03
 * Project: JumpRace
 */

public class TitleUtil {

    /**
     * Send a title packet to the player
     * @param player Player who should receive the packets
     * @param title Title text
     * @param fadeIn Time in ticks in which the title fades in
     * @param stay Time in ticks in which the title should stay
     * @param fadeOut Time in ticks in which the title fades out
     */
    public void sendTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}"), fadeIn, stay, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitle);
    }

    /**
     * Send a subtitle packet to the player
     * @param player Player who should receive the packets
     * @param subtitle Title text
     * @param fadeIn Time in ticks in which the title fades in
     * @param stay Time in ticks in which the title should stay
     * @param fadeOut Time in ticks in which the title fades out
     */
    public void sendSubTitle(Player player, String subtitle, int fadeIn, int stay, int fadeOut) {
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}"), fadeIn, stay, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitle);
    }

    /**
     * Send a title packet as well as a subtitle packet to the player
     * @param player Player who should receive the packets
     * @param title Title text
     * @param subtitle Subtitle text
     * @param fadeIn Time in ticks in which the title fades in
     * @param stay Time in ticks in which the title should stay
     * @param fadeOut Time in ticks in which the title fades out
     */
    public void sendFullTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.sendTitle(player, title, fadeIn, stay, fadeOut);
        this.sendSubTitle(player, subtitle, fadeIn, stay, fadeOut);
    }

}
