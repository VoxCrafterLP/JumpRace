package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.particlesystem.effects.ParticleEffect;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.04.2021
 * Time: 15:16
 * Project: JumpRace
 */

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        ParticleEffect effect = new ParticleEffectBuilder(EffectType.RING, ((Player) commandSender).getLocation(), EnumParticle.VILLAGER_HAPPY).setRotation(45, 45, 0).build();
        effect.startDrawing();

        Bukkit.getScheduler().runTaskLater(JumpRace.getInstance(), effect::stopDrawing, 400);

        return false;
    }
}
