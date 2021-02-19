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

    public void sendTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}"), fadeIn, stay, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitle);
    }

    public void sendSubTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}"), fadeIn, stay, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitle);
    }

    public void sendFullTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.sendTitle(player, title, fadeIn, stay, fadeOut);
        this.sendSubTitle(player, subtitle, fadeIn, stay, fadeOut);
    }

}
