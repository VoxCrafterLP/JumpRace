package com.voxcrafterlp.jumprace.builderserver.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 12:49
 * Project: JumpRace
 */

public class JumpRaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) return false;



        return false;
    }
}
